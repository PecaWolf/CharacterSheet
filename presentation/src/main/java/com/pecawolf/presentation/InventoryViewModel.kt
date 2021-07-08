package com.pecawolf.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.pecawolf.charactersheet.common.formatAmount
import com.pecawolf.model.Equipment
import com.pecawolf.model.Equipment.Item
import com.pecawolf.presentation.extensions.MergedLiveData2

class InventoryViewModel(val mainViewModel: MainViewModel) : ViewModel() {

    private val _loadoutType = MutableLiveData<Item.LoadoutType>().apply {
        value = Item.LoadoutType.COMBAT
    }
    private val _equipment = MutableLiveData<Equipment>()

    private val _money = MutableLiveData<Int>().apply {
        value = 50000
    }

    val loadoutType: LiveData<Item.LoadoutType> = _loadoutType
    val isPrimaryAllowed = MergedLiveData2<Equipment, Item.LoadoutType, Boolean>(
        _equipment,
        _loadoutType
    ) { equipment, type ->
        equipment.primary.allowedLoadouts.contains(type)
    }
    val isSecondaryAllowed = MergedLiveData2<Equipment, Item.LoadoutType, Boolean>(
        _equipment,
        _loadoutType
    ) { equipment, type ->
        equipment.secondary.allowedLoadouts.contains(type)
    }
    val isTertiaryAllowed = MergedLiveData2<Equipment, Item.LoadoutType, Boolean>(
        _equipment,
        _loadoutType
    ) { equipment, type ->
        equipment.tertiary.allowedLoadouts.contains(type)
    }
    val isClothingAllowed = MergedLiveData2<Equipment, Item.LoadoutType, Boolean>(
        _equipment,
        _loadoutType
    ) { equipment, type ->
        when (type) {
            Item.LoadoutType.COMBAT -> equipment.armor is Item.Armor.None
            Item.LoadoutType.SOCIAL -> true
            Item.LoadoutType.TRAVEL -> true
        }
    }
    val isArmorAllowed = MergedLiveData2<Equipment, Item.LoadoutType, Boolean>(
        _equipment,
        _loadoutType
    ) { equipment, type ->
        equipment.armor.allowedLoadouts.contains(type)
    }
    val equipment: LiveData<Equipment> = _equipment
    val money: LiveData<String> = Transformations.map(_money) { it.formatAmount() }

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
