package com.pecawolf.domain.interactor

import com.pecawolf.data.CharacterRepository
import com.pecawolf.model.Character

class GetCharacterInteractor(
    private val repository: CharacterRepository
) : MaybeInteractor<Nothing?, Character>() {

    override fun execute(params: Nothing?) = repository.getActiveCharacter()
}