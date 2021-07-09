package com.pecawolf.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pecawolf.model.Inventory
import com.pecawolf.model.Item
import com.pecawolf.presentation.extensions.MergedLiveData2

class InventoryViewModel(val mainViewModel: MainViewModel) : BaseViewModel() {

    private val _loadoutType = MutableLiveData<Item.LoadoutType>().apply {
        value = Item.LoadoutType.COMBAT
    }
    private val _inventory = MutableLiveData<Inventory>()

    val loadoutType: LiveData<Item.LoadoutType> = _loadoutType
    val isPrimaryAllowed = MergedLiveData2<Inventory, Item.LoadoutType, Boolean>(
        _inventory,
        _loadoutType
    ) { inventory, type ->
        inventory.primary.allowedLoadouts.contains(type)
    }
    val isSecondaryAllowed = MergedLiveData2<Inventory, Item.LoadoutType, Boolean>(
        _inventory,
        _loadoutType
    ) { inventory, type ->
        inventory.secondary.allowedLoadouts.contains(type)
    }
    val isTertiaryAllowed = MergedLiveData2<Inventory, Item.LoadoutType, Boolean>(
        _inventory,
        _loadoutType
    ) { inventory, type ->
        inventory.tertiary.allowedLoadouts.contains(type)
    }
    val isClothingAllowed = MergedLiveData2<Inventory, Item.LoadoutType, Boolean>(
        _inventory,
        _loadoutType
    ) { inventory, type ->
        when (type) {
            Item.LoadoutType.COMBAT -> inventory.armor is Item.Armor.None
            Item.LoadoutType.SOCIAL -> true
            Item.LoadoutType.TRAVEL -> true
        }
    }
    val isArmorAllowed = MergedLiveData2<Inventory, Item.LoadoutType, Boolean>(
        _inventory,
        _loadoutType
    ) { inventory, type ->
        inventory.armor.allowedLoadouts.contains(type)
    }
    val inventory: LiveData<Inventory> = _inventory

    fun onLoadoutCombatClicked() {
        _loadoutType.value = Item.LoadoutType.COMBAT
    }

    fun onLoadoutSocialClicked() {
        _loadoutType.value = Item.LoadoutType.SOCIAL
    }

    fun onLoadoutTravelClicked() {
        _loadoutType.value = Item.LoadoutType.TRAVEL
    }
}
