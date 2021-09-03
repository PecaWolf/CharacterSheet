package com.pecawolf.presentation.viewmodel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pecawolf.common.extensions.isOneOf
import com.pecawolf.domain.interactor.RollDiceInteractor
import com.pecawolf.model.Inventory
import com.pecawolf.model.Item
import com.pecawolf.model.Item.Armor
import com.pecawolf.model.Item.Weapon
import com.pecawolf.model.RollResult
import com.pecawolf.model.Rollable.Skill
import com.pecawolf.presentation.extensions.MergedLiveData2
import com.pecawolf.presentation.extensions.MergedLiveData6
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
    val isClothesAllowed = MergedLiveData2<Inventory, Item.LoadoutType, Boolean>(
        mainViewModel.inventory,
        _loadoutType
    ) { inventory, type ->
        when (type) {
            Item.LoadoutType.COMBAT -> inventory.armor is Armor.None
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
    val skills: LiveData<List<Skill>> = MergedLiveData6(
        mainViewModel.skills,
        mainViewModel.inventory,
        isPrimaryAllowed,
        isSecondaryAllowed,
        isTertiaryAllowed,
        isArmorAllowed,
    ) { skills, inventory, isPrimaryAllowed, isSecondaryAllowed, isTertiaryAllowed, isArmorAllowed ->
        listOfNotNull(
            inventory.primary.takeIf { isPrimaryAllowed },
            inventory.secondary.takeIf { isSecondaryAllowed },
            inventory.tertiary.takeIf { isTertiaryAllowed },
            inventory.armor.takeIf { isArmorAllowed },
        )
            .flatMap { item ->
                when (item) {
                    is Weapon.Ranged.Bow -> skills.dexterity.filter { it.code == Skill.Constants.ARCHERY }
                    is Weapon.Ranged.Crossbow -> skills.dexterity.filter { it.code == Skill.Constants.ARCHERY }
                    is Weapon.Ranged -> skills.dexterity.filter { it.code == Skill.Constants.FIREARMS }
                    is Weapon.Melee.Knife -> skills.dexterity.filter { it.code.isOneOf(Skill.Constants.SWORDFIGHTING, Skill.Constants.THROWING) }
                    is Weapon.Melee -> skills.dexterity.filter { it.code == Skill.Constants.SWORDFIGHTING }
                    is Armor -> if (item.damage == Item.Damage.HEAVY) skills.dexterity.filter { it.code == Skill.Constants.HEAVY_ARMOR } else listOf()
                    else -> listOf()
                }
            }
            .groupBy { it.code }
            .map { it.value.first() }
            .toMutableList()
            .apply {
                skills.dexterity.firstOrNull { it.code == Skill.Constants.UNARMED }
                    ?.let { add(it) }
            }
    }

    fun onLoadoutCombatClicked() {
        _loadoutType.value = Item.LoadoutType.COMBAT
    }

    fun onLoadoutSocialClicked() {
        _loadoutType.value = Item.LoadoutType.SOCIAL
    }

    fun onLoadoutTravelClicked() {
        _loadoutType.value = Item.LoadoutType.TRAVEL
    }

    fun onPrimaryClicked() {
        inventory.value?.primary?.itemId?.let {
            _navigateTo.postValue(Destination.ItemDetail(it))
        }
    }

    fun onSecondaryClicked() {
        inventory.value?.secondary?.itemId?.let {
            _navigateTo.postValue(Destination.ItemDetail(it))
        }
    }

    fun onTertiaryClicked() {
        inventory.value?.tertiary?.itemId?.let {
            _navigateTo.postValue(Destination.ItemDetail(it))
        }
    }

    fun onClothesClicked() {
        inventory.value?.clothes?.itemId?.let {
            _navigateTo.postValue(Destination.ItemDetail(it))
        }
    }

    fun onArmorClicked() {
        inventory.value?.armor?.itemId?.let {
            _navigateTo.postValue(Destination.ItemDetail(it))
        }
    }

    fun onRollClicked(skill: Skill) {
        _navigateTo.postValue(Destination.RollModifierDialog(skill))
    }

    fun onRollConfirmed(skill: Skill, modifier: String) {
        roll.execute(skill to modifier.toInt())
            .observe(ROLL + skill.code, ::onRollError, ::onRollSuccess)
    }

    private fun onRollSuccess(result: RollResult) {
        Timber.v("onRollSuccess(): $result")
        _navigateTo.postValue(
            Destination.RollResultDialog(result)
        )
    }

    private fun onRollError(error: Throwable) {
        Timber.e(error, "onRollError(): ")
    }

    sealed class Destination {
        data class RollModifierDialog(val skill: Skill) : Destination()
        data class RollResultDialog(val rollResult: RollResult) : Destination()
        data class ItemDetail(val itemId: Long) : Destination()
    }

    companion object {
        private const val ROLL = "ROLL_"
    }
}
