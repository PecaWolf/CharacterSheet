package com.pecawolf.presentation.viewmodel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pecawolf.common.extensions.isOneOf
import com.pecawolf.common.extensions.let2
import com.pecawolf.domain.interactor.RollDiceInteractor
import com.pecawolf.model.Inventory
import com.pecawolf.model.Item
import com.pecawolf.model.Item.Armor
import com.pecawolf.model.Item.Weapon
import com.pecawolf.model.RollResult
import com.pecawolf.model.Rollable
import com.pecawolf.presentation.extensions.MergedLiveData2
import com.pecawolf.presentation.extensions.SingleLiveEvent
import com.pecawolf.presentation.viewmodel.BaseViewModel
import timber.log.Timber

class LoadoutViewModel(
    private val mainViewModel: MainViewModel,
    private val roll: RollDiceInteractor
) : BaseViewModel() {

    private val _navigateTo = SingleLiveEvent<Destination>()
    private val _loadoutType = MutableLiveData<Item.LoadoutType>().apply {
        value = Item.LoadoutType.COMBAT
    }

    val navigateTo: LiveData<Destination> = _navigateTo
    val loadoutType: LiveData<Item.LoadoutType> = _loadoutType
    val isPrimaryAllowed = MergedLiveData2<Inventory, Item.LoadoutType, Boolean>(
        mainViewModel.inventory,
        _loadoutType
    ) { inventory, type ->
        inventory.primary.allowedLoadouts.contains(type)
    }
    val isSecondaryAllowed = MergedLiveData2<Inventory, Item.LoadoutType, Boolean>(
        mainViewModel.inventory,
        _loadoutType
    ) { inventory, type ->
        inventory.secondary.allowedLoadouts.contains(type)
    }
    val isTertiaryAllowed = MergedLiveData2<Inventory, Item.LoadoutType, Boolean>(
        mainViewModel.inventory,
        _loadoutType
    ) { inventory, type ->
        inventory.tertiary.allowedLoadouts.contains(type)
    }
    val isClothingAllowed = MergedLiveData2<Inventory, Item.LoadoutType, Boolean>(
        mainViewModel.inventory,
        _loadoutType
    ) { inventory, type ->
        when (type) {
            Item.LoadoutType.COMBAT -> inventory.armor is Item.Armor.None
            Item.LoadoutType.SOCIAL -> true
            Item.LoadoutType.TRAVEL -> true
        }
    }
    val isArmorAllowed = MergedLiveData2<Inventory, Item.LoadoutType, Boolean>(
        mainViewModel.inventory,
        _loadoutType
    ) { inventory, type ->
        inventory.armor.allowedLoadouts.contains(type)
    }
    val inventory: LiveData<Inventory> = mainViewModel.inventory
    val skills: LiveData<List<Rollable.Skill>> = MergedLiveData2(
        mainViewModel.skills,
        mainViewModel.inventory
    ) { skills, inventory ->
        listOf(
            inventory.primary,
            inventory.secondary,
            inventory.tertiary,
            inventory.armor,
        )
            .flatMap { item ->
                when (item) {
                    is Weapon.Ranged.Bow -> skills.dexterity.filter { it.code == "ARCH" }
                    is Weapon.Ranged.Crossbow -> skills.dexterity.filter { it.code == "ARCH" }
                    is Weapon.Ranged -> skills.dexterity.filter { it.code == "FRAM" }
                    is Weapon.Melee.BareHands -> skills.dexterity.filter { it.code == "UCMB" }
                    is Weapon.Melee.Knife -> skills.dexterity.filter { it.code.isOneOf("SWDF", "THRW") }
                    is Weapon.Melee -> skills.dexterity.filter { it.code == "SWDF" }
                    is Armor -> if (item.damage == Item.Damage.HEAVY) skills.dexterity.filter { it.code == "HVAC" } else listOf()
                    else -> listOf()
                }
            }
            .groupBy { it.code }
            .map { it.value.first() }
    }

    fun onLoadoutCombatClicked() {
        _loadoutType.value = com.pecawolf.model.Item.LoadoutType.COMBAT
    }

    fun onLoadoutSocialClicked() {
        _loadoutType.value = com.pecawolf.model.Item.LoadoutType.SOCIAL
    }

    fun onLoadoutTravelClicked() {
        _loadoutType.value = com.pecawolf.model.Item.LoadoutType.TRAVEL
    }

    fun onRollClicked(skill: Rollable.Skill) {
        _navigateTo.postValue(Destination.RollModifierDialog(skill))
    }

    fun onRollConfirmed(skill: Rollable.Skill, modifier: String) {
        roll.execute(skill to modifier.toInt())
            .observe(ROLL + skill.code, ::onRollError, ::onRollSuccess)
    }

    private fun onRollSuccess(result: Pair<Int, RollResult>) {
        Timber.v("onRollSuccess(): $result")
        _navigateTo.postValue(
            result.let2 { roll, rollResult -> Destination.RollResultDialog(roll, rollResult) }
        )
    }

    private fun onRollError(error: Throwable) {
        Timber.e(error, "onRollError(): ")
    }

    sealed class Destination {
        data class RollModifierDialog(val skill: Rollable.Skill) : Destination()
        data class RollResultDialog(val roll: Int, val rollResult: RollResult) : Destination()
    }

    companion object {
        private const val ROLL = "ROLL_"
    }
}
