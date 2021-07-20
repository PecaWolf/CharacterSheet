package com.pecawolf.presentation.viewmodel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import com.pecawolf.model.Item
import com.pecawolf.presentation.extensions.MergedLiveData2
import com.pecawolf.presentation.extensions.MergedLiveData3
import com.pecawolf.presentation.extensions.SingleLiveEvent
import com.pecawolf.presentation.extensions.notifyChanged
import com.pecawolf.presentation.viewmodel.BaseViewModel

class NewItemStep1ViewModel : BaseViewModel() {


    private val _nameInput = MutableLiveData("")
    private val _descriptionInput = MutableLiveData("")
    private val _selectedItem = MutableLiveData(Item.ItemType.NONE)
    private val _itemTypes = MutableLiveData(Item.ItemType.items())
    private val _navigateToNext = SingleLiveEvent<Triple<String, String, Item.ItemType>>()

    val nameInput = _nameInput.distinctUntilChanged()
    val descriptionInput = _descriptionInput.distinctUntilChanged()
    val itemTypes: LiveData<List<Pair<Item.ItemType, Boolean>>> =
        MergedLiveData2(_itemTypes, _selectedItem) { items, selectedItem ->
            items.map { Pair(it, it == selectedItem) }
        }
    val isNextEnabled: LiveData<Boolean> =
        MergedLiveData3(_nameInput, _descriptionInput, _selectedItem) { name, description, type ->
            name.isNotBlank() && description.isNotBlank() && type != Item.ItemType.NONE
        }
    val navigateToNext: LiveData<Triple<String, String, Item.ItemType>> = _navigateToNext

    override fun onRefresh() {
    }

    fun onNameChanged(name: String) {
        _nameInput.value = name
    }

    fun onDescriptionChanged(description: String) {
        _descriptionInput.value = description
    }

    fun onItemTypeSelected(type: Item.ItemType) {
        if (type == _selectedItem.value) _selectedItem.value = Item.ItemType.NONE
        else _selectedItem.value = type

        _itemTypes.notifyChanged()
    }

    fun onNextClicked() {
        _navigateToNext.postValue(
            Triple(
                _nameInput.value!!,
                _descriptionInput.value!!,
                _selectedItem.value!!,
            )
        )
    }
}