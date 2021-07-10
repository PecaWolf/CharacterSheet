package com.pecawolf.domain.interactor

import com.pecawolf.data.CharacterRepository

class ClearActiveCharacterInteractor(private val repository: CharacterRepository) :
    CompletableInteractor<Nothing?>() {

    override fun execute(params: Nothing?) = repository.clearActiveCharacter()
}
