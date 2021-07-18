package com.pecawolf.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.pecawolf.domain.interactor.GetItemDetailInteractor
import com.pecawolf.domain.interactor.SaveItemChangesInteractor
import com.pecawolf.model.Item
import com.pecawolf.presentation.extensions.MergedLiveData2
import com.pecawolf.presentation.extensions.SingleLiveEvent
import com.pecawolf.presentation.extensions.toggle
import com.pecawolf.presentation.viewmodel.main.MainViewModel
import timber.log.Timber

class ItemDetailViewModel(
    private val itemId: Long,
    private val mainViewModel: MainViewModel,
    private val getItemDetail: GetItemDetailInteractor,
    private val saveItemChanges: SaveItemChangesInteractor,
) : BaseViewModel() {

    private val _item = mainViewModel.character
        .map { it.inventory.backpack.first { it.itemId == itemId } }
    private val _isEditing = MutableLiveData<Boolean>(false)
    private val _navigateTo = SingleLiveEvent<Destination>()
    private val _showNotFound = SingleLiveEvent<Unit>()

    val item: LiveData<Item> = _item
    val damageTypes: LiveData<List<Pair<Item.DamageType, Boolean>>> = _item.map { item ->
        when (item) {
            is Item.Armor -> item.protections.map { Pair(it, true) }
            is Item.Weapon -> item.damageTypes.map { Pair(it, true) }
            else -> listOf()
        }
    }
    val isEditingBaseData: LiveData<Boolean> = _isEditing
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
    val navigateTo: LiveData<Destination> = _navigateTo
    val showNotFound: LiveData<Unit> = _showNotFound

//    override fun onRefresh() {
//        getItemDetail.execute(itemId)
//            .observe(FETCH, ::onGetItemError, _item::setValue, ::onGetItemComplete)
//    }

    fun onItemEquip(item: Item, slot: Item.Slot?) {
        if (slot == null) _navigateTo.postValue(Destination.EquipDialog(item))
        else _navigateTo.postValue(Destination.UnequipDialog(item, slot))
    }

    fun onItemEditClicked() {
        _isEditing.toggle()
    }

    private fun onGetItemComplete() {
        Timber.v("onGetItemComplete()")
        _showNotFound.postValue(Unit)
    }

    private fun onGetItemError(error: Throwable) {
        Timber.w(error, "onGetItemError(): ")
        _showNotFound.postValue(Unit)
    }

    fun onNameChanged(name: String) {
        _item.value?.also { item ->
            if (item.name == name) {
                item.name = name
                updateItem(item)
            }
        }
    }

    fun onDescriptionChanged(description: String) {
        _item.value?.also { item ->
            if (item.description == description) {
                item.description = description
                updateItem(item)
            }
        }
    }

    fun onCountChanged(count: Int) {
        _item.value?.also { item ->
            if (item.count == count) {
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
        _item.value
            ?.takeIf { it is Item.Weapon || it is Item.Armor }
            ?.also {
                _navigateTo.postValue(
                    Destination.MultiChoice(it.itemId, MultiChoiceViewModel.LOADOUT)
                )
            }
    }

    fun onDamageEditClicked() {
        _item.value
            ?.takeIf { it is Item.Weapon || it is Item.Armor }
            ?.also {
                _navigateTo.postValue(
                    Destination.MultiChoice(it.itemId, MultiChoiceViewModel.DAMAGE)
                )
            }
    }

    fun onWieldEditClicked() {
        _item.value
            ?.takeIf { it is Item.Weapon }
            ?.also {
                _navigateTo.postValue(
                    Destination.MultiChoice(it.itemId, MultiChoiceViewModel.WIELD)
                )
            }
    }

    fun onDamageTypesEditClicked() {
        _item.value
            ?.takeIf { it is Item.Weapon || it is Item.Armor }
            ?.also {
                _navigateTo.postValue(
                    Destination.MultiChoice(it.itemId, MultiChoiceViewModel.DAMAGE_TYPE)
                )
            }
    }

    private fun updateItem(it: Item) = saveItemChanges.execute(it)
        .observe(UPDATE, ::onUpdateItemError, ::onUpdateItemSuccess)

    private fun onUpdateItemSuccess() {
        Timber.v("onUpdateItemSuccess()")
    }

    private fun onUpdateItemError(error: Throwable) {
        Timber.w(error, "onUpdateItemError(): ")
    }

    sealed class Destination {
        data class MultiChoice(val itemId: Long, val field: String) : Destination()
        data class EquipDialog(val item: Item) : Destination()
        data class UnequipDialog(val item: Item, val slot: Item.Slot) : Destination()
    }

    companion object {
        private const val FETCH = "FETCH"
        private const val UPDATE = "UPDATE"
    }
}
