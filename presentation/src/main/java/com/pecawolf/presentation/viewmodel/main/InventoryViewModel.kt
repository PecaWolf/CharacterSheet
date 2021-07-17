package com.pecawolf.presentation.viewmodel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.pecawolf.domain.interactor.EquipItemInteractor
import com.pecawolf.domain.interactor.UnequipItemInteractor
import com.pecawolf.model.Item
import com.pecawolf.presentation.extensions.SingleLiveEvent
import com.pecawolf.presentation.viewmodel.BaseViewModel
import timber.log.Timber

class InventoryViewModel(
    private val mainViewModel: MainViewModel,
    private val equipItem: EquipItemInteractor,
    private val unequipItem: UnequipItemInteractor
) : BaseViewModel() {

    private val _navigateTo = SingleLiveEvent<Destination>()

    val navigateTo: LiveData<Destination> = _navigateTo

    val backpack: LiveData<List<Pair<Item, Item.Slot?>>> =
        Transformations.map(mainViewModel.character) { character ->
            character.inventory.backpack
                .map {
                    Pair(
                        it,
                        when (it.itemId) {
                            character.inventory.primary.itemId -> Item.Slot.PRIMARY
                            character.inventory.secondary.itemId -> Item.Slot.SECONDARY
                            character.inventory.tertiary.itemId -> Item.Slot.TERTIARY
                            //                    character.inventory.grenade.itemId -> Item.Slot.GRENADE
                            character.inventory.armor.itemId -> Item.Slot.ARMOR
                            character.inventory.clothes.itemId -> Item.Slot.CLOTHING
                            else -> null
                        }
                    )
                }
                .sortedWith(
                    compareBy({ it.second?.ordinal ?: Int.MAX_VALUE }, { it.first.name }),
                )
        }

    val money: LiveData<Int> = Transformations.map(mainViewModel.character) {
        it.inventory.money
    }

    override fun onRefresh() {

    }

    fun onItemEdit(itemId: Long) {
        _navigateTo.postValue(Destination.ItemDetail(itemId))
    }

    fun onItemEquip(item: Item, slot: Item.Slot?) {
        if (slot == null) _navigateTo.postValue(Destination.EquipDialog(item))
        else _navigateTo.postValue(Destination.UnequipDialog(item, slot))
    }

    fun onAddItem() {
        _navigateTo.postValue(Destination.NewItem)
    }

    fun onEquipSlotSelected(itemId: Long, slot: Item.Slot) {
        equipItem.execute(itemId to slot)
            .observe(EQUIP, ::onEquipItemError, ::onEquipItemSuccess)
    }

    fun onUnequipItemConfirmed(slot: Item.Slot) {
        unequipItem.execute(slot)
            .observe(UNEQUIP, ::onEquipItemError, ::onEquipItemSuccess)
    }

    private fun onEquipItemSuccess() {
        Timber.v("onEquipItemSuccess()")
    }

    private fun onEquipItemError(error: Throwable) {
        Timber.w(error, "onEquipItemError(): ")
    }

    sealed class Destination {
        object NewItem : Destination()
        data class ItemDetail(val itemId: Long) : Destination()
        data class EquipDialog(val item: Item) : Destination()
        data class UnequipDialog(val item: Item, val slot: Item.Slot) : Destination()
    }

    companion object {
        private const val EQUIP = "EQUIP"
        private const val UNEQUIP = "UNEQUIP"
    }
}
