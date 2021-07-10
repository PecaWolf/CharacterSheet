package com.pecawolf.presentation.viewmodel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pecawolf.domain.interactor.SubscribeToActiveCharacter
import com.pecawolf.model.Character
import com.pecawolf.presentation.viewmodel.BaseViewModel
import timber.log.Timber

class MainViewModel(private val subscribeToActiveCharacter: SubscribeToActiveCharacter) :
    BaseViewModel() {

    private val _character = MutableLiveData<Character>()
    val character: LiveData<Character> = _character

    init {
        onRefresh()
    }

    override fun onRefresh() {
        subscribeToActiveCharacter.execute(null)
            .observe(LOADING_CHARACTER, ::onGetCharacterError, ::onGetCharacterSuccess)
    }

    private fun onGetCharacterSuccess(wrapper: List<Character>) {
        wrapper.firstOrNull()?.let {
            _character.value = it
        }
    }

    private fun onGetCharacterError(error: Throwable) {
        Timber.e(error, "onGetCharacterError(): ")
    }

    companion object {
        const val LOADING_CHARACTER = "LOADING_CHARACTER"
    }
}