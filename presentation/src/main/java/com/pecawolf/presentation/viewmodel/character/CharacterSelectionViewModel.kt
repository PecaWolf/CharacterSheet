package com.pecawolf.presentation.viewmodel.character

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pecawolf.domain.interactor.GetCharacterInteractor
import com.pecawolf.domain.interactor.GetCharactersInteractor
import com.pecawolf.domain.interactor.SetActiveCharacterIdInteractor
import com.pecawolf.model.CharacterSnippet
import com.pecawolf.presentation.extensions.SingleLiveEvent
import com.pecawolf.presentation.extensions.notifyChanged
import com.pecawolf.presentation.viewmodel.BaseViewModel
import timber.log.Timber

class CharacterSelectionViewModel(
    private val getActiveCharacter: GetCharacterInteractor,
    private val getCharacters: GetCharactersInteractor,
    private val setActiveCharacterId: SetActiveCharacterIdInteractor
) : BaseViewModel() {

    private val _characters = MutableLiveData<List<CharacterSnippet>>()
    private val _navigateTo = SingleLiveEvent<Destination>()

    val characters: LiveData<List<CharacterSnippet>> = _characters
    val navigateTo: LiveData<Destination> = _navigateTo

    override fun onRefresh() {
        Timber.v("onRefresh()")
        getActiveCharacter.execute(null)
            .observe(LOADING_GET_ACTIVE, ::onGetActiveCharacterError, ::onGetActiveCharacterSuccess)
    }

    fun onExistingCharacterClicked(characterId: Long) {
        setActiveCharacterId.execute(characterId)
            .observe(LOADING_SET_ID, ::onSetIdError, ::onSetIdSuccess)
    }

    fun onNewCharacterClicked() {
        _navigateTo.postValue(Destination.Create)
    }

    private fun onGetActiveCharacterSuccess() {
        Timber.v("onGetActiveCharacterSuccess()")
        _navigateTo.postValue(Destination.Home)
    }

    private fun onGetActiveCharacterError(error: Throwable) {
        Timber.e(error, "onGetActiveCharacterError(): ")
        getCharacters.execute(null)
            .observe(LOADING_GET_CHARACTERS, ::onGetCharactersError, ::onGetCharactersSuccess)
    }

    private fun onGetCharactersSuccess(list: List<CharacterSnippet>) {
        Timber.v("onGetCharactersSuccess(): ${list.size}")

        _characters.value = list
        _characters.notifyChanged()
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
        const val LOADING_GET_ACTIVE = "LOADING_GET_ACTIVE"
        const val LOADING_GET_CHARACTERS = "LOADING_GET_CHARACTERS"
        const val LOADING_SET_ID = "LOADING_SET_ID"
    }
}