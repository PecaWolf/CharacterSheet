package com.pecawolf.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.pecawolf.domain.interactor.GetItemDetailInteractor
import com.pecawolf.domain.interactor.SaveItemChanges
import com.pecawolf.model.Item
import com.pecawolf.presentation.extensions.SingleLiveEvent
import com.pecawolf.presentation.extensions.toggle
import com.pecawolf.presentation.viewmodel.main.InventoryViewModel
import com.pecawolf.presentation.viewmodel.main.MainViewModel
import timber.log.Timber

class ItemDetailViewModel(
    private val itemId: Long,
    private val mainViewModel: MainViewModel,
    private val getItemDetail: GetItemDetailInteractor,
    private val saveItemChanges: SaveItemChanges,
) : BaseViewModel() {

    private val _item = mainViewModel.character
        .map { it.inventory.backpack.first { it.itemId == itemId } }
    private val _isEditing = MutableLiveData<Boolean>(false)
    private val _navigateTo = SingleLiveEvent<InventoryViewModel.Destination>()
    private val _showNotFound = SingleLiveEvent<Unit>()

    val item: LiveData<Item> = _item
    val isEditing: LiveData<Boolean> = _isEditing
    val navigateTo: LiveData<InventoryViewModel.Destination> = _navigateTo
    val showNotFound: LiveData<Unit> = _showNotFound

//    override fun onRefresh() {
//        getItemDetail.execute(itemId)
//            .observe(FETCH, ::onGetItemError, _item::setValue, ::onGetItemComplete)
//    }

    fun onItemEquip(item: Item, slot: Item.Slot?) {
        if (slot == null) _navigateTo.postValue(InventoryViewModel.Destination.EquipDialog(item))
        else _navigateTo.postValue(InventoryViewModel.Destination.UnequipDialog(item, slot))
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
            item.name = name
            updateItem(item)
        }
    }

    fun onDescriptionChanged(description: String) {
        _item.value?.also { item ->
            item.description = description
            updateItem(item)
        }
    }

    fun onCountChanged(count: Int) {
        _item.value?.also { item ->
            item.count = count
            updateItem(item)
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

    companion object {
        private const val FETCH = "FETCH"
        private const val UPDATE = "UPDATE"
    }
}
