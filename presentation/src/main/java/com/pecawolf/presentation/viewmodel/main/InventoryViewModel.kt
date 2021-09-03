package com.pecawolf.presentation.viewmodel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import com.pecawolf.domain.interactor.UpdateMoneyInteractor
import com.pecawolf.model.Item
import com.pecawolf.presentation.extensions.SingleLiveEvent
import com.pecawolf.presentation.viewmodel.BaseViewModel
import timber.log.Timber

class InventoryViewModel(
    private val mainViewModel: MainViewModel,
    private val updateMoney: UpdateMoneyInteractor
) : BaseViewModel() {

    private val _navigateTo = SingleLiveEvent<Destination>()

    val navigateTo: LiveData<Destination> = _navigateTo

    val backpack: LiveData<List<Pair<Item, Item.Slot?>>> =
        mainViewModel.inventory.map { inventory ->
            inventory.backpack
                .map {
                    Pair(
                        it,
                        when (it.itemId) {
                            inventory.primary.itemId -> Item.Slot.PRIMARY
                            inventory.secondary.itemId -> Item.Slot.SECONDARY
                            inventory.tertiary.itemId -> Item.Slot.TERTIARY
                            //                    inventory.grenade.itemId -> Item.Slot.GRENADE
                            inventory.armor.itemId -> Item.Slot.ARMOR
                            inventory.clothes.itemId -> Item.Slot.CLOTHES
                            else -> null
                        }
                    )
                }
                .sortedWith(
                    compareBy(
                        { it.second?.ordinal ?: Int.MAX_VALUE },
                        { it.first.name },
                        { it.first.itemId }),
                )
        }.distinctUntilChanged()

    val money: LiveData<Int> = mainViewModel.inventory.map {
        it.money
    }

    fun onItemDetailClicked(itemId: Long) {
        _navigateTo.postValue(Destination.ItemDetail(itemId))
    }

    fun onMoneyClicked() {
        money.value?.also {
            _navigateTo.postValue(Destination.EditMoney(it))
        }
    }

    fun onMoneyConfirmed(newMoney: Int) {
        updateMoney.execute(newMoney)
            .observe(UPDATE_MONEY, ::onUpdateMoneyError, ::onUpdateMoneySuccess)
    }

    private fun onUpdateMoneySuccess() {
        Timber.v("onUpdateMoneySuccess()")
    }

    private fun onUpdateMoneyError(error: Throwable) {
        Timber.e(error, "onUpdateMoneyError(): ")
    }

    fun onAddItemClicked() {
        _navigateTo.postValue(Destination.NewItem)
    }

    sealed class Destination {
        object NewItem : Destination()
        data class ItemDetail(val itemId: Long) : Destination()
        data class EditMoney(val money: Int) : Destination()
    }

    companion object {
        private const val UPDATE_MONEY = "UPDATE_MONEY"
    }
}
