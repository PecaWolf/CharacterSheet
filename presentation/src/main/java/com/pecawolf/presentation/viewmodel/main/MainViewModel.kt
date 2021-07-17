package com.pecawolf.presentation.viewmodel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pecawolf.domain.interactor.GetCharacterInteractor
import com.pecawolf.domain.interactor.SubscribeToActiveCharacter
import com.pecawolf.model.Character
import com.pecawolf.presentation.viewmodel.BaseViewModel
import timber.log.Timber

class MainViewModel(
    private val subscribeToActiveCharacter: SubscribeToActiveCharacter,
    private val getActiveCharacter: GetCharacterInteractor
) :
    BaseViewModel() {

    private val _character = MutableLiveData<Character>()
    val character: LiveData<Character> = _character

    init {
        subscribeToActiveCharacter.execute(null)
            .observe(
                SUBSCRIBE,
                ::onGetCharacterError,
                ::onGetCharacterSuccess,
                ::onGetCharacterComplete
            )

        onRefresh()
    }

    override fun onRefresh() {
        getActiveCharacter.execute(null)
            .observe(REFRESH, ::onGetCharacterError, ::onGetCharacterSuccess)
    }

    private fun onGetCharacterComplete() {
        Timber.v("onGetCharacterComplete():")

    }

    private fun onGetCharacterSuccess(character: Character) {
        Timber.v("onGetCharacterSuccess():")
        _character.value = character
    }

    private fun onGetCharacterError(error: Throwable) {
        Timber.e(error, "onGetCharacterError(): ")
    }

    companion object {
        const val SUBSCRIBE = "SUBSCRIBE"
        const val REFRESH = "REFRESH"
    }
}