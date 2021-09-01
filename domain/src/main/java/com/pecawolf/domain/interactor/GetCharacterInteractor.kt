package com.pecawolf.domain.interactor

import com.pecawolf.domain.repository.ICharacterRepository
import com.pecawolf.model.Character
import io.reactivex.rxjava3.core.Maybe
import timber.log.Timber

class GetCharacterInteractor(
    private val repository: ICharacterRepository,
) : MaybeInteractor<Nothing?, Character>() {

    override fun execute(params: Nothing?) = repository.observeActiveCharacter()
        .firstElement()
        .onErrorResumeNext {
            Timber.e(it, "execute(): ")
            Maybe.empty()
        }
}