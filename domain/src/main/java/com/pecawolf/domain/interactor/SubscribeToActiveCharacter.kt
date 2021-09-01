package com.pecawolf.domain.interactor

import com.pecawolf.domain.repository.ICharacterRepository
import com.pecawolf.model.Character
import io.reactivex.rxjava3.core.Observable

class SubscribeToActiveCharacter(
    private val repository: ICharacterRepository,
) : ObservableInteractor<Nothing?, Character>() {

    override fun execute(params: Nothing?): Observable<Character> =
        repository.observeActiveCharacter()
}

