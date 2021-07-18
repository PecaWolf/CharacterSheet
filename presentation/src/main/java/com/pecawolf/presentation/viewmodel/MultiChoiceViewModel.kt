package com.pecawolf.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pecawolf.charactersheet.common.extensions.isOneOf
import com.pecawolf.domain.interactor.GetItemDetailInteractor
import com.pecawolf.domain.interactor.SaveItemChangesInteractor
import com.pecawolf.model.Item
import com.pecawolf.presentation.extensions.SingleLiveEvent
import com.pecawolf.presentation.extensions.mapNotNull
import com.pecawolf.presentation.extensions.notifyChanged
import timber.log.Timber

class MultiChoiceViewModel(
    private val itemId: Long,
    field: String,
    private val getItem: GetItemDetailInteractor,
    private val saveItemChanges: SaveItemChangesInteractor
) : BaseViewModel() {

    private val _item = MutableLiveData<Item>()
    private val _cancel = SingleLiveEvent<Unit>()

    val field: LiveData<String> = MutableLiveData(field)

    val loadoutOptions: LiveData<List<Pair<Item.LoadoutType, Boolean>>> = _item.mapNotNull { item ->
        val allowedLoadouts = when (item) {
            is Item.Armor -> item.allowedLoadouts
            is Item.Weapon -> item.allowedLoadouts
            else -> throw IllegalStateException("Damage type selection for non-weapon and non-armor item: ${item::class.java.simpleName}!")
        }

        Item.LoadoutType.values().map { Pair(it, it.isOneOf(allowedLoadouts)) }
            .takeIf { field == LOADOUT }
    }

    val damageTypes: LiveData<List<Pair<Item.DamageType, Boolean>>> = _item.mapNotNull { item ->
        val damageTypes = when (item) {
            is Item.Armor -> item.protections
            is Item.Weapon -> item.damageTypes
            else -> throw IllegalStateException("Damage type selection for non-weapon and non-armor item: ${item::class.java.simpleName}!")
        }

        Item.DamageType.values().map { Pair(it, it.isOneOf(damageTypes)) }
            .takeIf { field == DAMAGE_TYPE }
            ?.sortedWith(compareBy({ !it.second }, { it.first.ordinal }))
    }

    val damageLevels: LiveData<List<Pair<Item.Damage, Boolean>>> = _item.mapNotNull { item ->
        val damage = when (item) {
            is Item.Armor -> item.damageMitigation
            is Item.Weapon -> item.damage
            else -> throw IllegalStateException("Damage selection for non-weapon and non-armor item: ${item::class.java.simpleName}!")
        }

        Item.Damage.values().map { Pair(it, it == damage) }
            .takeIf { field == DAMAGE }
    }

    val wieldOptions: LiveData<List<Pair<Item.Weapon.Wield, Boolean>>> = _item.mapNotNull { item ->
        val wield = when (item) {
            is Item.Weapon -> item.wield
            else -> throw IllegalStateException("Damage selection for non-weapon item: ${item::class.java.simpleName}!")
        }

        Item.Weapon.Wield.values()
            .filter { it != Item.Weapon.Wield.DRONE }
            .map { Pair(it, it == wield) }
            .takeIf { field == WIELD }
    }

    val cancel: LiveData<Unit> = _cancel

    override fun onRefresh() {
        getItem.execute(itemId)
            .observe(FETCH, ::onGetItemDetailError, ::onGetItemDetailSuccess)
    }

    fun itemSelected(selectedItem: Any) {
        when (field.value) {
            LOADOUT -> {
                when (_item.value) {
                    is Item.Weapon -> {
                        (_item.value as Item.Weapon).run {
                            val loadout = selectedItem as Item.LoadoutType
                            if (allowedLoadouts.contains(loadout)) {
                                allowedLoadouts.remove(loadout)
                            } else {
                                allowedLoadouts.add(loadout)
                            }
                            _item.notifyChanged()
                        }
                    }
                    is Item.Armor -> {
                        (_item.value as Item.Armor).run {
                            val loadout = selectedItem as Item.LoadoutType
                            if (allowedLoadouts.contains(loadout)) {
                                allowedLoadouts.remove(loadout)
                            } else {
                                allowedLoadouts.add(loadout)
                            }
                            _item.notifyChanged()
                        }
                    }
                }
            }
            DAMAGE_TYPE -> {
                when (_item.value) {
                    is Item.Weapon -> {
                        (_item.value as Item.Weapon).run {
                            val damageType = selectedItem as Item.DamageType
                            if (damageTypes.contains(damageType)) {
                                damageTypes.remove(damageType)
                            } else {
                                damageTypes.add(damageType)
                            }
                            _item.notifyChanged()
                        }
                    }
                    is Item.Armor -> {
                        (_item.value as Item.Armor).run {
                            val damageType = selectedItem as Item.DamageType
                            if (protections.contains(damageType)) {
                                protections.remove(damageType)
                            } else {
                                protections.add(damageType)
                            }
                            _item.notifyChanged()
                        }
                    }
                }
            }
            DAMAGE -> {
                when (_item.value) {
                    is Item.Weapon -> {
                        (_item.value as Item.Weapon).run {
                            damage = selectedItem as Item.Damage
                            _item.notifyChanged()
                        }
                    }
                    is Item.Armor -> {
                        (_item.value as Item.Armor).run {
                            damageMitigation = selectedItem as Item.Damage
                            _item.notifyChanged()
                        }
                    }
                }
            }
            WIELD -> {
                (_item.value as? Item.Weapon)?.run {
                    wield = selectedItem as Item.Weapon.Wield
                    _item.notifyChanged()
                }
            }
        }
    }

    fun onPositiveButtonClicked() {
        _item.value?.let {
            saveItemChanges.execute(it)
                .observe(SAVE, ::onSaveItemChangesError, ::onSaveItemChangesSuccess)
        }
    }

    private fun onSaveItemChangesSuccess() {
        Timber.v("onSaveItemChangesSuccess()")
        _cancel.postValue(Unit)
    }

    private fun onSaveItemChangesError(error: Throwable) {
        Timber.w(error, "onSaveItemChangesError(): ")
    }

    fun onNegativeButtonClicked() {
        _cancel.postValue(Unit)
    }

    private fun onGetItemDetailSuccess(item: Item) {
        Timber.v("onGetItemDetailSuccess(): $item")
        _item.value = item
    }

    private fun onGetItemDetailError(error: Throwable) {
        Timber.e(error, "onGetItemDetailError(): ")
    }

    companion object {
        private const val FETCH = "FETCH"
        private const val SAVE = "SAVE"

        const val LOADOUT = "LOADOUT"
        const val DAMAGE_TYPE = "DAMAGE_TYPE"
        const val DAMAGE = "DAMAGE"
        const val WIELD = "WIELD"
    }
}
