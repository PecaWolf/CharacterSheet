package com.pecawolf.presentation.viewmodel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import com.pecawolf.common.extensions.isOneOf
import com.pecawolf.domain.interactor.CreateNewItemInteractor
import com.pecawolf.model.Item
import com.pecawolf.presentation.extensions.MergedLiveData3
import com.pecawolf.presentation.extensions.MergedLiveData5
import com.pecawolf.presentation.extensions.SingleLiveEvent
import com.pecawolf.presentation.extensions.add
import com.pecawolf.presentation.extensions.remove
import com.pecawolf.presentation.extensions.toggle
import com.pecawolf.presentation.viewmodel.BaseViewModel
import timber.log.Timber

class NewItemStep2ViewModel(
    private val name: String,
    private val description: String,
    private val type: Item.ItemType,
    private val createNewItem: CreateNewItemInteractor
) : BaseViewModel() {

    private val _selectedType = MutableLiveData(type)
    private val _loadoutCombatChecked = MutableLiveData(true)
    private val _loadoutSocialChecked = MutableLiveData(
        type.isOneOf(
            Item.ItemType.PISTOL,
            Item.ItemType.KNIFE,
            Item.ItemType.CLOTHES
        )
    )
    private val _loadoutTravelChecked = MutableLiveData(
        type.isOneOf(
            Item.ItemType.PISTOL,
            Item.ItemType.KNIFE,
            Item.ItemType.CLOTHES
        )
    )
    private val _selectedLoadouts = MergedLiveData3(
        _loadoutCombatChecked, _loadoutSocialChecked, _loadoutTravelChecked
    ) { isCombatChecked, isSocialChecked, isTravelChecked ->
        listOfNotNull(
            Item.LoadoutType.COMBAT.takeIf { isCombatChecked },
            Item.LoadoutType.SOCIAL.takeIf { isSocialChecked },
            Item.LoadoutType.TRAVEL.takeIf { isTravelChecked }
        )
    }
    private val _damageLightChecked = MutableLiveData(type.defaultDamage == Item.Damage.LIGHT)
    private val _damageMediumChecked =
        MutableLiveData(type.defaultDamage == Item.Damage.MEDIUM)
    private val _damageHeavyChecked =
        MutableLiveData(type.defaultDamage == Item.Damage.HEAVY)
    private val _damageSelected: LiveData<Item.Damage> = MergedLiveData3(
        _damageLightChecked,
        _damageMediumChecked,
        _damageHeavyChecked
    ) { isLightChecked, isMediumChecked, isHeavyChecked ->
        when {
            isLightChecked -> Item.Damage.LIGHT
            isMediumChecked -> Item.Damage.MEDIUM
            isHeavyChecked -> Item.Damage.HEAVY
            else -> Item.Damage.NONE
        }
    }
    private val _magazineSize = MutableLiveData<Int>(-1)
    private val _rateOfFire = MutableLiveData<Int>(-1)
    private val _magazineCount = MutableLiveData<Int>(-1)
    private val _magazineState = MutableLiveData<Int>(-1)
    private val _selectedDamageTypes = MutableLiveData(type.defaultDamageTypes.toMutableSet())
    private val _selectedWield =
        MutableLiveData<Item.Weapon.Wield>(type.defaultWield)
    private val _navigateTo = SingleLiveEvent<Destination>()

    val isWieldVisible: LiveData<Boolean> = _selectedType.map { type ->
        type.isWeapon
    }
    val isDamageTypesVisible: LiveData<Boolean> = _selectedType.map { type ->
        type.isWeapon || type.isArmor
    }
    val isAmmoVisible: LiveData<Boolean> = _selectedType.map { type ->
        type.isRanged
    }
    val loadoutCombatChecked: LiveData<Boolean> = _loadoutCombatChecked
    val loadoutSocialChecked: LiveData<Boolean> = _loadoutSocialChecked
    val loadoutTravelChecked: LiveData<Boolean> = _loadoutTravelChecked
    val damageLightChecked: LiveData<Boolean> = _damageLightChecked.map {
        if (it) {
            _damageMediumChecked.value = false
            _damageHeavyChecked.value = false
        }

        it
    }
    val damageMediumChecked: LiveData<Boolean> = _damageMediumChecked.map {
        if (it) {
            _damageLightChecked.value = false
            _damageHeavyChecked.value = false
        }
        it
    }
    val damageHeavyChecked: LiveData<Boolean> = _damageHeavyChecked.map {
        if (it) {
            _damageLightChecked.value = false
            _damageMediumChecked.value = false
        }
        it
    }
    val magazineSize: LiveData<String> = _magazineSize
        .distinctUntilChanged()
        .map { it.takeIf { it > 0 }?.toString() ?: "" }
    val rateOfFire: LiveData<String> = _rateOfFire
        .distinctUntilChanged()
        .map { it.takeIf { it > 0 }?.toString() ?: "" }
    val damageTypes: LiveData<List<Pair<Item.DamageType, Boolean>>> =
        _selectedDamageTypes.map { selected ->
            Item.DamageType.values()
                .sortedBy { it.name }
                .map { type ->
                    Pair(type, type.isOneOf(selected))
                }
        }
    val selectedWield: LiveData<Item.Weapon.Wield> = _selectedWield
    val isWeapon: LiveData<Boolean> = _selectedType.map {
        it.isWeapon
    }
    val isNextEnabled: LiveData<Boolean> = MergedLiveData5(
        _selectedLoadouts,
        _damageSelected,
        _magazineSize,
        _rateOfFire,
        _selectedDamageTypes
    ) { loadouts, damage, magazine, rateOfFire, damageTypes ->
        val loadoutsSelected = loadouts.isNotEmpty()
        val validDamage =
            (type.isWeapon && damage != Item.Damage.NONE && damageTypes.isNotEmpty()) || !type.isWeapon
        val validRanged = (type.isRanged && magazine > 0 && rateOfFire > 0) || !type.isRanged

        loadoutsSelected && validDamage && validRanged
    }
    val navigateTo: LiveData<Destination> = _navigateTo

    fun loadoutCombatClicked() {
        Timber.v("loadoutCombatClicked()")
        _loadoutCombatChecked.toggle()
    }

    fun loadoutSocialClicked() {
        Timber.v("loadoutSocialClicked()")
        _loadoutSocialChecked.toggle()
    }

    fun loadoutTravelClicked() {
        Timber.v("loadoutTravelClicked()")
        _loadoutTravelChecked.toggle()
    }

    fun damageLightClicked() {
        Timber.v("damageLightClicked()")
        _damageLightChecked.toggle()
    }

    fun damageMediumClicked() {
        Timber.v("damageMediumClicked()")
        _damageMediumChecked.toggle()
    }

    fun damageHeavyClicked() {
        Timber.v("damageHeavyClicked()")
        _damageHeavyChecked.toggle()
    }

    fun onMagazineSizeInputChanged(input: Int) {
        Timber.v("onMagazineSizeInputChanged(): $input")
        _magazineSize.value = input
    }

    fun onRateOfFireInputChanged(input: Int) {
        Timber.v("onRateOfFireInputChanged(): $input")
        _rateOfFire.value = input
    }

    fun onDamageTypeSelected(damageType: Item.DamageType) {
        Timber.v("onDamageTypeSelected(): $damageType")
        if (_selectedDamageTypes.value?.contains(damageType) == true) {
            _selectedDamageTypes.remove(damageType)
        } else {
            _selectedDamageTypes.add(damageType)
        }
    }

    fun onWieldSelected(wield: Item.Weapon.Wield) {
        Timber.v("onWieldSelected(): $wield")
        _selectedWield.value = wield
    }

    fun onNextClicked() {
        createNewItem.execute(
            CreateNewItemInteractor.Params(
                name = name,
                description = description,
                type = type,
                loadouts = _selectedLoadouts.value!!,
                damage = _damageSelected.value!!,
                wield = _selectedWield.value,
                magazineSize = _magazineSize.value ?: -1,
                rateOfFire = _rateOfFire.value ?: -1,
                _magazineCount.value ?: -1,
                _magazineState.value ?: -1,
                damageTypes = _selectedDamageTypes.value!!,
            )
        )
            .observe(SAVE, ::onCreateNewItemError, ::onCreateNewItemSuccess)
    }

    private fun onCreateNewItemSuccess(itemId: Long) {
        Timber.v("onCreateNewItemSuccess(): $itemId")
        _navigateTo.postValue(Destination.Overview(itemId))
    }

    private fun onCreateNewItemError(error: Throwable) {
        Timber.e(error, "onCreateNewItemError(): ")
    }

    sealed class Destination() {
        data class Overview(val itemId: Long) : Destination()
    }

    companion object {
        private const val SAVE = "SAVE"
    }
}