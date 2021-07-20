package com.pecawolf.presentation.viewmodel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import com.pecawolf.model.Item
import com.pecawolf.presentation.extensions.SingleLiveEvent
import com.pecawolf.presentation.viewmodel.BaseViewModel

class InventoryViewModel(
    private val mainViewModel: MainViewModel
) : BaseViewModel() {

    private val _navigateTo = SingleLiveEvent<Destination>()

    val navigateTo: LiveData<Destination> = _navigateTo

    val backpack: LiveData<List<Pair<Item, Item.Slot?>>> =
        mainViewModel.inventory.map { inventory ->
            inventory.backpack
                .map {
                    Pair(
                        it,
                        when (it.itemId) {
                            inventory.primary.itemId -> Item.Slot.PRIMARY
                            inventory.secondary.itemId -> Item.Slot.SECONDARY
                            inventory.tertiary.itemId -> Item.Slot.TERTIARY
                            //                    inventory.grenade.itemId -> Item.Slot.GRENADE
                            inventory.armor.itemId -> Item.Slot.ARMOR
                            inventory.clothes.itemId -> Item.Slot.CLOTHING
                            else -> null
                        }
                    )
                }
                .sortedWith(
                    compareBy(
                        { it.second?.ordinal ?: Int.MAX_VALUE },
                        { it.first.name },
                        { it.first.itemId }),
                )
        }.distinctUntilChanged()

    val money: LiveData<Int> = mainViewModel.inventory.map {
        it.money
    }

    override fun onRefresh() {

    }

    fun onItemEdit(itemId: Long) {
        _navigateTo.postValue(Destination.ItemDetail(itemId))
    }

    fun onAddItem() {
        _navigateTo.postValue(Destination.NewItem)
    }

    sealed class Destination {
        object NewItem : Destination()
        data class ItemDetail(val itemId: Long) : Destination()
    }

    companion object {
    }
}
