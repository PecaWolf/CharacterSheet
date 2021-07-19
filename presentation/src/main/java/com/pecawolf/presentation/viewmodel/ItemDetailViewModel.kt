package com.pecawolf.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.pecawolf.charactersheet.common.extensions.isOneOf
import com.pecawolf.charactersheet.common.extensions.setAll
import com.pecawolf.domain.interactor.SaveItemChangesInteractor
import com.pecawolf.model.Item
import com.pecawolf.presentation.extensions.MergedLiveData2
import com.pecawolf.presentation.extensions.SingleLiveEvent
import com.pecawolf.presentation.extensions.mapNotNull
import com.pecawolf.presentation.extensions.toggle
import com.pecawolf.presentation.viewmodel.main.MainViewModel
import timber.log.Timber

class ItemDetailViewModel(
    private val itemId: Long,
    private val mainViewModel: MainViewModel,
    private val saveItemChanges: SaveItemChangesInteractor,
) : BaseViewModel() {

    private val _item = mainViewModel.inventory
        .mapNotNull {
            it.backpack.firstOrNull {
                it.itemId == itemId
            }
        }
    private val _isEditing = MutableLiveData<Boolean>(false)
    private val _showLoadoutDialog = SingleLiveEvent<List<Pair<Item.LoadoutType, Boolean>>>()
    private val _showDamageDialog = SingleLiveEvent<List<Pair<Item.Damage, Boolean>>>()
    private val _showWieldDialog = SingleLiveEvent<List<Pair<Item.Weapon.Wield, Boolean>>>()
    private val _showDamageTypesDialog = SingleLiveEvent<List<Pair<Item.DamageType, Boolean>>>()
    private val _navigateTo = SingleLiveEvent<Destination>()

    val item: LiveData<Item> = _item
    val damageTypes: LiveData<List<Pair<Item.DamageType, Boolean>>> = _item.map { item ->
        when (item) {
            is Item.Armor -> item.damageTypes.map { Pair(it, true) }
            is Item.Weapon -> item.damageTypes.map { Pair(it, true) }
            else -> listOf()
        }
    }
    val isEditingBaseData: LiveData<Boolean> = _isEditing
    val isEditingCount: LiveData<Boolean> = MergedLiveData2(_isEditing, _item) { isEditing, item ->
        isEditing && item is Item.Other || item is Item.Weapon.Grenade
    }
    val isEditingLoadoutAndDamage: LiveData<Boolean> =
        MergedLiveData2(_isEditing, _item) { isEditing, item ->
            isEditing && item is Item.Weapon || item is Item.Armor
        }
    val isEditingAmmunition: LiveData<Boolean> =
        MergedLiveData2(_isEditing, _item) { isEditing, item ->
            isEditing && item is Item.Weapon.Ranged
        }
    val isEditingWield: LiveData<Boolean> = MergedLiveData2(_isEditing, _item) { isEditing, item ->
        isEditing && item is Item.Weapon.Melee
    }
    val showLoadoutDialog: LiveData<List<Pair<Item.LoadoutType, Boolean>>> = _showLoadoutDialog
    val showDamageDialog: LiveData<List<Pair<Item.Damage, Boolean>>> = _showDamageDialog
    val showWieldDialog: LiveData<List<Pair<Item.Weapon.Wield, Boolean>>> = _showWieldDialog
    val showDamageTypesDialog: LiveData<List<Pair<Item.DamageType, Boolean>>> =
        _showDamageTypesDialog
    val navigateTo: LiveData<Destination> = _navigateTo

    fun onItemEquip(item: Item, slot: Item.Slot?) {
//        if (slot == null) _navigateTo.postValue(Destination.EquipDialog(item))
//        else _navigateTo.postValue(Destination.UnequipDialog(item, slot))
    }

    fun onItemEditClicked() {
        _isEditing.toggle()
    }

    fun onNameChanged(name: String) {
        _item.value?.also { item ->
            if (item.name != name) {
                item.name = name
                updateItem(item)
            }
        }
    }

    fun onDescriptionChanged(description: String) {
        _item.value?.also { item ->
            if (item.description != description) {
                item.description = description
                updateItem(item)
            }
        }
    }

    fun onCountChanged(count: Int) {
        _item.value?.also { item ->
            if (item.count != count) {
                item.count = count
                updateItem(item)
            }
        }
    }

    fun onMagazineSizeChanged(magazine: Int) {
        (_item.value as? Item.Weapon.Ranged)?.also { item ->
            if (item.magazine != magazine) {
                item.magazine = magazine
                updateItem(item)
            }
        }
    }

    fun onRateOfFireChanged(rateOfFire: Int) {
        (_item.value as? Item.Weapon.Ranged)?.also { item ->
            if (item.rateOfFire != rateOfFire) {
                item.rateOfFire = rateOfFire
                updateItem(item)
            }
        }
    }

    fun onLoadoutEditClicked() {
        _item.value?.also { item ->
            _showLoadoutDialog.postValue(
                Item.LoadoutType.values().map {
                    it to (it.isOneOf(item.allowedLoadouts))
                }
            )
        }
    }

    fun onLoadoutChanged(loadoutType: List<Item.LoadoutType>) {
        _item.value?.also { item ->
            item.allowedLoadouts.setAll(loadoutType)
            updateItem(item)
        }
    }

    fun onDamageEditClicked() {
        _item.value?.also { item ->
            _showDamageDialog.postValue(
                Item.Damage.values().map {
                    it to (it == item.damage)
                }
            )
        }
    }

    fun onDamageChanged(damage: Item.Damage) {
        _item.value?.also { item ->
            item.damage = damage
            updateItem(item)
        }
    }

    fun onWieldEditClicked() {
        (_item.value as? Item.Weapon)?.also { item ->
            _showWieldDialog.postValue(
                Item.Weapon.Wield.values().map {
                    it to (it == item.wield)
                }
            )
        }
    }

    fun onWieldChanged(wield: Item.Weapon.Wield) {
        (_item.value as? Item.Weapon)?.also { item ->
            item.wield = wield
            updateItem(item)
        }
    }

    fun onDamageTypesEditClicked() {
        _item.value?.also { item ->
            _showDamageTypesDialog.postValue(
                Item.DamageType.values().map {
                    it to (it.isOneOf(item.damageTypes))
                }
            )
        }
    }

    fun onDamageTypesChanged(damageTypes: List<Item.DamageType>) {
        _item.value?.also { item ->
            item.damageTypes.setAll(damageTypes)
            updateItem(item)
        }
    }

    private fun updateItem(item: Item) = saveItemChanges.execute(item)
        .observe(UPDATE, ::onUpdateItemError, ::onUpdateItemSuccess)

    private fun onUpdateItemSuccess() {
        Timber.v("onUpdateItemSuccess()")
    }

    private fun onUpdateItemError(error: Throwable) {
        Timber.w(error, "onUpdateItemError(): ")
    }

    sealed class Destination {
    }

    companion object {
        private const val FETCH = "FETCH"
        private const val UPDATE = "UPDATE"
    }
}
