package com.pecawolf.presentation.viewmodel

import androidx.lifecycle.LiveData
import com.pecawolf.domain.interactor.GetCharactersInteractor
import com.pecawolf.domain.interactor.SetActiveCharacterIdInteractor
import com.pecawolf.model.CharacterSnippet
import com.pecawolf.presentation.extensions.SingleLiveEvent
import timber.log.Timber

class ChooseCharacterViewModel(
    private val getCharacters: GetCharactersInteractor,
    private val setActiveCharacterId: SetActiveCharacterIdInteractor
) : BaseViewModel() {

    private val _navigateTo = SingleLiveEvent<Destination>()

    val navigateTo: LiveData<Destination> = _navigateTo

    init {
        onRefresh()
    }

    private fun onRefresh() {
        getCharacters.execute(null)
            .observe(LOADING_GET_CHARACTERS, ::onGetCharactersError, ::onGetCharactersSuccess)
    }

    fun onExistingCharacterClicked(characterId: Long) {
        setActiveCharacterId.execute(characterId)
            .observe(LOADING_SET_ID, ::onSetIdError, ::onSetIdSuccess)
    }

    fun onNewCharacterClicked() {
        _navigateTo.postValue(Destination.Create)
    }

    private fun onGetCharactersSuccess(list: List<CharacterSnippet>?) {
        Timber.v("onGetCharactersSuccess(): ${list?.size}")
    }

    private fun onGetCharactersError(throwable: Throwable) {
        Timber.e(throwable, "onGetCharactersError(): ")
    }

    private fun onSetIdSuccess() {
        Timber.v("onSetIdSuccess()")
        _navigateTo.postValue(Destination.Home)
    }

    private fun onSetIdError(throwable: Throwable) {
        Timber.e(throwable, "onSetIdError(): ")

    }

    sealed class Destination {
        object Home : Destination()
        object Create : Destination()
    }

    companion object {
        const val LOADING_GET_CHARACTERS = "LOADING_GET_CHARACTERS"
        const val LOADING_SET_ID = "LOADING_SET_ID"
    }
}