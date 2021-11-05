package com.pecawolf.presentation.viewmodel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pecawolf.domain.interactor.RollDiceInteractor
import com.pecawolf.domain.interactor.SaveItemChangesInteractor
import com.pecawolf.model.Inventory
import com.pecawolf.model.Item
import com.pecawolf.model.Item.Armor
import com.pecawolf.model.RollResult
import com.pecawolf.model.Rollable.Skill
import com.pecawolf.model.Skills
import com.pecawolf.presentation.extensions.MergedLiveData2
import com.pecawolf.presentation.extensions.SingleLiveEvent
import com.pecawolf.presentation.viewmodel.BaseViewModel
import timber.log.Timber

class LoadoutViewModel(
    private val mainViewModel: MainViewModel,
    private val roll: RollDiceInteractor,
    private val saveItemChanges: SaveItemChangesInteractor,
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
    val primary = MergedLiveData2<Inventory, Skills, Pair<Item, Skill?>>(
        mainViewModel.inventory, mainViewModel.skills,
    ) { inventory, skills ->
        inventory.primary.let {
            it to getAppropriateSkill(it, skills)
        }
    }
    val secondary = MergedLiveData2<Inventory, Skills, Pair<Item, Skill?>>(
        mainViewModel.inventory, mainViewModel.skills,
    ) { inventory, skills ->
        inventory.secondary.let {
            it to getAppropriateSkill(it, skills)
        }
    }
    val tertiary = MergedLiveData2<Inventory, Skills, Pair<Item, Skill?>>(
        mainViewModel.inventory, mainViewModel.skills,
    ) { inventory, skills ->
        inventory.tertiary.let {
            it to getAppropriateSkill(it, skills)
        }
    }
    val armor = MergedLiveData2<Inventory, Skills, Pair<Item, Skill?>>(
        mainViewModel.inventory, mainViewModel.skills,
    ) { inventory, skills ->
        inventory.armor.let {
            it to getAppropriateSkill(it, skills)
        }
    }
    val clothes = MergedLiveData2<Inventory, Skills, Pair<Item, Skill?>>(
        mainViewModel.inventory, mainViewModel.skills,
    ) { inventory, skills ->
        inventory.clothes.let {
            it to getAppropriateSkill(it, skills)
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
        primary.value?.first?.itemId?.let {
            _navigateTo.postValue(Destination.ItemDetail(it))
        }
    }

    fun onPrimaryReloadClicked() {
        (primary.value?.first as? Item.Weapon.Ranged)?.apply {
            magazineState = magazineSize
            magazineCount = maxOf(magazineCount - 1, 0)
            updateItem(this)
        }
    }

    fun onSecondaryClicked() {
        secondary.value?.first?.itemId?.let {
            _navigateTo.postValue(Destination.ItemDetail(it))
        }
    }

    fun onSecondaryReloadClicked() {
        (secondary.value?.first as? Item.Weapon.Ranged)?.apply {
            magazineState = magazineSize
            magazineCount = maxOf(magazineCount - 1, 0)
            updateItem(this)
        }
    }

    fun onTertiaryClicked() {
        tertiary.value?.first?.itemId?.let {
            _navigateTo.postValue(Destination.ItemDetail(it))
        }
    }

    fun onTertiaryReloadClicked() {
        (tertiary.value?.first as? Item.Weapon.Ranged)?.apply {
            magazineState = magazineSize
            magazineCount = maxOf(magazineCount - 1, 0)
            updateItem(this)
        }
    }

    fun onClothesClicked() {
        clothes.value?.first?.itemId?.let {
            _navigateTo.postValue(Destination.ItemDetail(it))
        }
    }

    fun onArmorClicked() {
        armor.value?.first?.itemId?.let {
            _navigateTo.postValue(Destination.ItemDetail(it))
        }
    }

    fun onRollClicked(skill: Skill, item: Item?) {
        _navigateTo.postValue(Destination.RollModifierDialog(skill, item))
    }

    fun onRollConfirmed(skill: Skill, modifier: String, item: Item?) {
        roll.execute(skill to modifier.toInt())
            .observe(ROLL + skill.code, ::onRollError, { onRollSuccess(it, item) })
    }

    private fun onRollSuccess(result: RollResult, item: Item?) {
        Timber.v("onRollSuccess(): $result")
        Timber.i("onRollSuccess(): $item")
        _navigateTo.postValue(Destination.RollResultDialog(result))
        (item as? Item.Weapon.Ranged)?.let { ranged ->
            ranged.magazineState = maxOf(ranged.magazineState - ranged.rateOfFire, 0)
            updateItem(ranged)
        }
    }

    private fun onRollError(error: Throwable) {
        Timber.e(error, "onRollError(): ")
    }

    private fun updateItem(item: Item) = saveItemChanges.execute(item)
        .observe(UPDATE + item.name.uppercase(), ::onUpdateItemError, ::onUpdateItemSuccess)

    private fun onUpdateItemSuccess() {
        Timber.v("onUpdateItemSuccess()")
    }

    private fun onUpdateItemError(error: Throwable) {
        Timber.e(error, "onUpdateItemError(): ")
    }

    private fun getAppropriateSkill(item: Item, skills: Skills): Skill? {
        return when (item) {
            is Item.Weapon.Ranged.Bow -> skills.dexterity.firstOrNull { it.code == Skill.Constants.ARCHERY }
            is Item.Weapon.Ranged.Crossbow -> skills.dexterity.firstOrNull { it.code == Skill.Constants.ARCHERY }
            is Item.Weapon.Ranged -> skills.dexterity.firstOrNull { it.code == Skill.Constants.FIREARMS }
            is Item.Weapon.Melee.Knife -> skills.dexterity.firstOrNull { it.code == Skill.Constants.SWORDFIGHTING }
            is Item.Weapon.Melee.BareHands -> skills.dexterity.firstOrNull { it.code == Skill.Constants.UNARMED }
            is Item.Weapon.Melee -> skills.dexterity.firstOrNull { it.code == Skill.Constants.SWORDFIGHTING }
            is Armor -> if (item.damage == Item.Damage.HEAVY) skills.dexterity.firstOrNull { it.code == Skill.Constants.HEAVY_ARMOR } else null
            else -> null
        }
    }

    sealed class Destination {
        data class RollModifierDialog(val skill: Skill, val item: Item?) : Destination()
        data class RollResultDialog(val rollResult: RollResult) : Destination()
        data class ItemDetail(val itemId: Long) : Destination()
    }

    companion object {
        private const val ROLL = "ROLL_"
        private const val UPDATE = "UPDATE_"
    }
}
