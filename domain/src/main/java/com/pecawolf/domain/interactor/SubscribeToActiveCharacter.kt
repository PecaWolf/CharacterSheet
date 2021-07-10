package com.pecawolf.domain.interactor

import com.pecawolf.data.CharacterRepository
import com.pecawolf.model.Character

class SubscribeToActiveCharacter(
    private val repository: CharacterRepository
) : ObservableInteractor<Nothing?, Character>() {

    override fun execute(params: Nothing?) = repository.observeActiveCharacter()
}

