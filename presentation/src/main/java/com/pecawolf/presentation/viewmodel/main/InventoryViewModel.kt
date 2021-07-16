package com.pecawolf.presentation.viewmodel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.pecawolf.model.Item
import com.pecawolf.presentation.extensions.SingleLiveEvent
import com.pecawolf.presentation.viewmodel.BaseViewModel

class InventoryViewModel(private val mainViewModel: MainViewModel) : BaseViewModel() {

    private val _navigateTo = SingleLiveEvent<Destination>()

    val navigateTo: LiveData<Destination> = _navigateTo

    val backpack: LiveData<List<Item>> = Transformations.map(mainViewModel.character) {
        it.inventory.backpack
    }

    val storage: LiveData<List<Item>> = Transformations.map(mainViewModel.character) {
        it.inventory.storage
    }

    val money: LiveData<Int> = Transformations.map(mainViewModel.character) {
        it.inventory.money
    }

    override fun onRefresh() {

    }

    fun onItemEdit(itemId: Long) {
        _navigateTo.postValue(Destination.ItemDetail(itemId))
    }

    fun onItemSwitch(itemId: Long, fromBackpack: Boolean) {

    }

    fun onAddItem() {
        _navigateTo.postValue(Destination.NewItem)
    }

    sealed class Destination {
        object NewItem : Destination()
        data class ItemDetail(val itemId: Long) : Destination()
    }
}
