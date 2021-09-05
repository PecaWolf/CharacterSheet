package com.pecawolf.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.pecawolf.common.extensions.isOneOf
import com.pecawolf.common.extensions.setAll
import com.pecawolf.domain.interactor.DeleteItemInteractor
import com.pecawolf.domain.interactor.EquipItemInteractor
import com.pecawolf.domain.interactor.SaveItemChangesInteractor
import com.pecawolf.domain.interactor.UnequipItemInteractor
import com.pecawolf.model.Item
import com.pecawolf.model.Item.Armor
import com.pecawolf.model.Item.Damage
import com.pecawolf.model.Item.DamageType
import com.pecawolf.model.Item.LoadoutType
import com.pecawolf.model.Item.Weapon
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
    private val equipItem: EquipItemInteractor,
    private val unequipItem: UnequipItemInteractor,
    private val deleteItem: DeleteItemInteractor
) : BaseViewModel() {

    private val _item = mainViewModel.inventory
        .mapNotNull {
            Timber.v("onChanged(): ${it.backpack.size}")
            it.backpack.firstOrNull {
                it.itemId == itemId
            }.also { Timber.v("onChanged(): $it") }
        }
    private val _isEditing = MutableLiveData<Boolean>(false)
    private val _navigateTo = SingleLiveEvent<Destination>()

    val item: LiveData<Item> = _item
    val slot: LiveData<List<Item.Slot>> = mainViewModel.inventory.map { inventory ->
        when (itemId) {
            inventory.primary.itemId -> listOf(Item.Slot.PRIMARY)
            inventory.secondary.itemId -> listOf(Item.Slot.SECONDARY)
            inventory.tertiary.itemId -> listOf(Item.Slot.TERTIARY)
            //                    inventory.grenade.itemId -> listOf(Item.Slot.GRENADE)
            inventory.armor.itemId -> listOf(Item.Slot.ARMOR)
            inventory.clothes.itemId -> listOf(Item.Slot.CLOTHES)
            else -> listOf()
        }.also { Timber.v("onChanged(): $it") }
    }
    val damageTypes: LiveData<List<Pair<DamageType, Boolean>>> = _item.map { item ->
        when (item) {
            is Armor -> item.damageTypes.map { Pair(it, true) }
            is Weapon -> item.damageTypes.map { Pair(it, true) }
            else -> listOf()
        }
    }
    val isEquippable: LiveData<Boolean> = _item.map { it.allowedSlots.isNotEmpty() }
    val isEditingBaseData: LiveData<Boolean> = _isEditing
    val isEditingCount: LiveData<Boolean> = MergedLiveData2(_isEditing, _item) { isEditing, item ->
        isEditing && item is Item.Other || item is Weapon.Grenade
    }
    val isEditingLoadoutAndDamage: LiveData<Boolean> =
        MergedLiveData2(_isEditing, _item) { isEditing, item ->
            isEditing && (item is Weapon || item is Armor)
        }
    val isEditingAmmunition: LiveData<Boolean> =
        MergedLiveData2(_isEditing, _item) { isEditing, item ->
            isEditing && item is Weapon.Ranged
        }
    val isEditingWield: LiveData<Boolean> = MergedLiveData2(_isEditing, _item) { isEditing, item ->
        isEditing && item is Weapon.Melee
    }
    val navigateTo: LiveData<Destination> = _navigateTo

    fun onItemEditClicked() {
        _isEditing.toggle()
    }

    fun onItemEquipClicked() {
        _item.value?.also { item ->
            if (slot.value.isNullOrEmpty()) _navigateTo.postValue(
                Destination.EquipConfirmDialog(
                    item.name,
                    item.allowedSlots
                )
            )
            else _navigateTo.postValue(
                Destination.UnequipConfirmDialog(
                    item.name,
                    slot.value!!.first()
                )
            )

        }
    }

    fun onEquipSlotSelected(slot: Item.Slot) {
        equipItem.execute(itemId to slot)
            .observe(EQUIP, ::onEquipItemError, ::onEquipItemSuccess)
    }

    fun onUnequipItemConfirmed() {
        slot.value?.firstOrNull()?.let { slot ->
            unequipItem.execute(slot)
                .observe(UNEQUIP, ::onEquipItemError, ::onEquipItemSuccess)
        }
    }

    fun onItemDeleteClicked() {
        _item.value?.let { item ->
            _navigateTo.postValue(Destination.DeleteConfirmDialog(item.name))
        }
    }

    fun onNameEditClicked() {
        _item.value?.let { item ->
            _navigateTo.postValue(Destination.NameDialog(item.name))
        }
    }

    fun onDescriptionEditClicked() {
        _item.value?.let { item ->
            _navigateTo.postValue(Destination.DescriptionDialog(item.description))
        }
    }

    fun onCountEditClicked() {
        _item.value?.let { item ->
            _navigateTo.postValue(Destination.CountDialog(item.count))
        }
    }

    fun onMagazineSizeEditClicked() {
        (_item.value as? Weapon.Ranged)?.let { item ->
            _navigateTo.postValue(Destination.MagazineSizeDialog(item.magazine))
        }
    }

    fun onRateOfFireEditClicked() {
        (_item.value as? Weapon.Ranged)?.let { item ->
            _navigateTo.postValue(Destination.RateOfFireDialog(item.rateOfFire))
        }
    }

    fun onItemDeleteConfirmed(price: Int) {
        _item.value?.also {
            deleteItem.execute(it.itemId to price)
                .observe(DELETE, ::onDeleteItemError, ::onDeleteItemSuccess)
        }
    }

    private fun onDeleteItemSuccess() {
        Timber.v("onDeleteItemSuccess()")
        _navigateTo.postValue(Destination.Leave)
    }

    private fun onDeleteItemError(error: Throwable) {
        Timber.e(error, "onDeleteItemError(): ")
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
        (_item.value as? Weapon.Ranged)?.also { item ->
            if (item.magazine != magazine) {
                item.magazine = magazine
                updateItem(item)
            }
        }
    }

    fun onRateOfFireChanged(rateOfFire: Int) {
        (_item.value as? Weapon.Ranged)?.also { item ->
            if (item.rateOfFire != rateOfFire) {
                item.rateOfFire = rateOfFire
                updateItem(item)
            }
        }
    }

    fun onLoadoutEditClicked() {
        _item.value?.also { item ->
            _navigateTo.postValue(Destination.LoadoutDialog(
                LoadoutType.values().map {
                    it to (it.isOneOf(item.allowedLoadouts))
                }
            ))
        }
    }

    fun onLoadoutChanged(loadoutType: List<LoadoutType>) {
        _item.value?.also { item ->
            item.allowedLoadouts.setAll(loadoutType)
            updateItem(item)
        }
    }

    fun onDamageEditClicked() {
        _item.value?.also { item ->
            _navigateTo.postValue(Destination.DamageDialog(
                Damage.values().map {
                    it to (it == item.damage)
                }
            ))
        }
    }

    fun onDamageChanged(damage: Damage) {
        _item.value?.also { item ->
            item.damage = damage
            updateItem(item)
        }
    }

    fun onWieldEditClicked() {
        (_item.value as? Weapon)?.also { item ->
            _navigateTo.postValue(Destination.WieldDialog(
                Weapon.Wield.values().map {
                    it to (it == item.wield)
                }
            ))
        }
    }

    fun onWieldChanged(wield: Weapon.Wield) {
        (_item.value as? Weapon)?.also { item ->
            item.wield = wield
            updateItem(item)
        }
    }

    fun onDamageTypesEditClicked() {
        _item.value?.also { item ->
            _navigateTo.postValue(Destination.DamageTypesDialog(
                DamageType.values().map {
                    it to (it.isOneOf(item.damageTypes))
                }
            ))
        }
    }

    fun onDamageTypesChanged(damageTypes: List<DamageType>) {
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
        Timber.e(error, "onUpdateItemError(): ")
    }

    private fun onEquipItemSuccess() {
        Timber.v("onEquipItemSuccess()")
    }

    private fun onEquipItemError(error: Throwable) {
        Timber.e(error, "onEquipItemError(): ")
    }

    sealed class Destination {
        data class CountDialog(val count: Int) : Destination()
        data class DamageDialog(val items: List<Pair<Damage, Boolean>>) : Destination()
        data class DamageTypesDialog(val items: List<Pair<DamageType, Boolean>>) : Destination()
        data class DeleteConfirmDialog(val name: String) : Destination()
        data class DescriptionDialog(val description: String) : Destination()
        data class EquipConfirmDialog(
            val name: String,
            val slots: List<com.pecawolf.model.Item.Slot>,
        ) :
            Destination()

        data class UnequipConfirmDialog(val name: String, val slot: com.pecawolf.model.Item.Slot) :
            Destination()

        object Leave : Destination()
        data class LoadoutDialog(val items: List<Pair<LoadoutType, Boolean>>) : Destination()
        data class MagazineSizeDialog(val magazine: Int) : Destination()
        data class NameDialog(val name: String) : Destination()
        data class RateOfFireDialog(val rateOfFire: Int) : Destination()
        data class WieldDialog(val items: List<Pair<Weapon.Wield, Boolean>>) : Destination()
    }

    companion object {
        private const val UPDATE = "UPDATE"
        private const val EQUIP = "EQUIP"
        private const val UNEQUIP = "UNEQUIP"
        private const val DELETE = "DELETE"
    }
}
