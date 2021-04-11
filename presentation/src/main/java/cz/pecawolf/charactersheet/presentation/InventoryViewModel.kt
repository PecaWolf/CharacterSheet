package cz.pecawolf.charactersheet.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import cz.pecawolf.charactersheet.common.model.Equipment
import cz.pecawolf.charactersheet.common.model.Equipment.Item
import cz.pecawolf.charactersheet.presentation.extensions.MergedLiveData2
import cz.pecawolf.charactersheet.presentation.extensions.toggle

class InventoryViewModel(val mainViewModel: MainViewModel) : ViewModel() {

    private val _isLoadoutCombat = MutableLiveData<Boolean>().apply {
        value = false
    }
    private val _equipment = MutableLiveData<Equipment>().apply {
        value = Equipment(
            Item.Weapon.Ranged.Rifle(
                "AR-15",
                "Awesome assault rifle",
                damageTypes = setOf(Equipment.DamageType.BALLISTIC)
            ),
            Item.Weapon.Ranged.Pistol(
                "Glock",
                "Awesome pistol",
                damageTypes = setOf(Equipment.DamageType.BALLISTIC)
            ),
            Item.Weapon.Melee.Sword(
                "Falcata",
                "Awesome tactical sword"
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

    val isLoadoutCombat: LiveData<Boolean> = _isLoadoutCombat
    val isPrimaryAllowed = MergedLiveData2<Equipment, Boolean, Boolean>(_equipment, _isLoadoutCombat){ equipment, isCombat ->
        Log.d("HECK", "primary: $isCombat, ${equipment.primary.isCivilian}")
        isCombat || equipment.primary.isCivilian
    }
    val isSecondaryAllowed = MergedLiveData2<Equipment, Boolean, Boolean>(_equipment, _isLoadoutCombat){ equipment, isCombat ->
        Log.d("HECK", "secondary: $isCombat, ${equipment.secondary.isCivilian}")
        isCombat || equipment.secondary.isCivilian
    }
    val isTertiaryAllowed = MergedLiveData2<Equipment, Boolean, Boolean>(_equipment, _isLoadoutCombat){ equipment, isCombat ->
        Log.d("HECK", "tertiary: $isCombat, ${equipment.tertiary.isCivilian}")
        isCombat || equipment.tertiary.isCivilian
    }
    val equipment: LiveData<Equipment> = _equipment
    val money: LiveData<String> = Transformations.map(_money) { "$it" }

    fun onSwitchLoadoutClicked() {
        _isLoadoutCombat.toggle()
    }
}
