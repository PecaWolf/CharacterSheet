package com.pecawolf.domain.interactor

import com.pecawolf.data.CharacterRepository
import com.pecawolf.model.Character
import io.reactivex.rxjava3.core.Observable

class SubscribeToActiveCharacter(
    private val repository: CharacterRepository
) : ObservableInteractor<Nothing?, List<Character>>() {

    override fun execute(params: Nothing?): Observable<List<Character>> =
        repository.observeActiveCharacter()
}

