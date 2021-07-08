package cz.pecawolf.charactersheet.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import cz.pecawolf.charactersheet.common.formatAmount
import cz.pecawolf.charactersheet.common.model.Equipment
import cz.pecawolf.charactersheet.common.model.Equipment.Item
import cz.pecawolf.charactersheet.presentation.extensions.MergedLiveData2

class InventoryViewModel(val mainViewModel: MainViewModel) : ViewModel() {

    private val _loadoutType = MutableLiveData<Item.LoadoutType>().apply {
        value = Item.LoadoutType.COMBAT
    }
    private val _equipment = MutableLiveData<Equipment>().apply {
        value = Equipment(
            Item.Weapon.Ranged.Rifle(
                "AR-15",
                "Awesome assault rifle"
            ),
            Item.Weapon.Ranged.Pistol(
                "Glock",
                "Awesome pistol"
            ),
            Item.Weapon.Melee.Sword(
                "Falcata",
                "Awesome tactical sword",
                allowedLoadouts = listOf(Item.LoadoutType.COMBAT, Item.LoadoutType.TRAVEL)
            ),
            Item.Armor.Clothing(
                "Kevlar-reinforced suit",
                "Style AND protection",
            ),
            Item.Armor.VacArmor(
                "Light vac-armor",
                "Kick ass anywhere"
            )
        )
    }

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
