package com.pecawolf.presentation.viewmodel.main

import androidx.lifecycle.LiveData
import com.pecawolf.domain.interactor.ClearActiveCharacterInteractor
import com.pecawolf.presentation.extensions.SingleLiveEvent
import com.pecawolf.presentation.viewmodel.BaseViewModel
import timber.log.Timber

class OtherViewModel(private val clearActiveCharacter: ClearActiveCharacterInteractor) :
    BaseViewModel() {

    private val _navigateTo = SingleLiveEvent<Destination>()

    val navigateTo: LiveData<Destination> = _navigateTo

    fun onSelectCharacterClicked() {
        clearActiveCharacterAndLeave(Destination.SelectCharacter)
    }

    fun onCreateCharacterClicked() {
        clearActiveCharacterAndLeave(Destination.CreateCharacter)
    }

    private fun clearActiveCharacterAndLeave(destination: Destination) {
        clearActiveCharacter.execute(null)
            .observe(
                CLEAR_ACTIVE_CHARACTER,
                ::onClearActiveCharacterError
            ) { onClearActiveCharacterSuccess(destination) }
    }

    private fun onClearActiveCharacterSuccess(destination: Destination) {
        Timber.v("onClearActiveCharacterSuccess()")
        _navigateTo.postValue(destination)
    }

    private fun onClearActiveCharacterError(error: Throwable) {
        Timber.e(error, "onClearActiveCharacterError(): ")
    }

    sealed class Destination {
        object CreateCharacter : Destination()
        object SelectCharacter : Destination()
    }

    companion object {
        const val CLEAR_ACTIVE_CHARACTER = "CLEAR_ACTIVE_CHARACTER"
    }
}