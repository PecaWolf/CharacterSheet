package com.pecawolf.domain.interactor

import com.pecawolf.domain.repository.ICharacterRepository

class ClearActiveCharacterInteractor(private val repository: ICharacterRepository) :
    CompletableInteractor<Nothing?>() {

    override fun execute(params: Nothing?) = repository.clearActiveCharacter()
}
