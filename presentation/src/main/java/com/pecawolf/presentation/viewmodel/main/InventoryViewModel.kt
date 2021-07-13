package com.pecawolf.presentation.viewmodel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.pecawolf.model.Item
import com.pecawolf.presentation.viewmodel.BaseViewModel

class InventoryViewModel(val mainViewModel: MainViewModel) : BaseViewModel() {

    val backpack: LiveData<List<Item>> = Transformations.map(mainViewModel.character) {
        it.inventory.backpack
    }

    val storage: LiveData<List<Item>> = Transformations.map(mainViewModel.character) {
        it.inventory.storage
    }

    val money: LiveData<Int> = Transformations.map(mainViewModel.character) {
        it.inventory.money
    }
}
