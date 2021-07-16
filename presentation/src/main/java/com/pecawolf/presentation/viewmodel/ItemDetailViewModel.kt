package com.pecawolf.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pecawolf.domain.interactor.GetItemDetailInteractor
import com.pecawolf.model.Item
import com.pecawolf.presentation.extensions.SingleLiveEvent
import timber.log.Timber

class ItemDetailViewModel(
    private val itemId: Long,
    private val getItemDetail: GetItemDetailInteractor
) : BaseViewModel() {

    private val _item = MutableLiveData<Item>()
    private val _showNotFound = SingleLiveEvent<Unit>()

    val item: LiveData<Item> = _item
    val showNotFound: LiveData<Unit> = _showNotFound

    override fun onRefresh() {
        getItemDetail.execute(itemId)
            .observe(FETCH, ::onGetItemError, _item::setValue, ::onGetItemComplete)
    }

    private fun onGetItemComplete() {
        Timber.v("onGetItemComplete()")
        _showNotFound.postValue(Unit)
    }

    private fun onGetItemError(error: Throwable) {
        Timber.w(error, "onGetItemError(): ")
        _showNotFound.postValue(Unit)
    }

    companion object {
        private const val FETCH = "FETCH"
    }
}
