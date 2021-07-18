package com.pecawolf.domain.interactor

import com.pecawolf.data.CharacterRepository
import com.pecawolf.model.Character
import io.reactivex.rxjava3.core.Maybe
import timber.log.Timber

class GetCharacterInteractor(
    private val repository: CharacterRepository
) : MaybeInteractor<Nothing?, Character>() {

    override fun execute(params: Nothing?) = repository.observeActiveCharacter()
        .firstElement()
        .onErrorResumeNext {
            Timber.w(it, "execute(): ")
            Maybe.empty()
        }
}