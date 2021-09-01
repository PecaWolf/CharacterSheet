package com.pecawolf.domain.interactor

import com.pecawolf.domain.repository.ICharacterRepository
import com.pecawolf.model.Character

class ObserveCharacterInteractor(
    private val repository: ICharacterRepository,
) : ObservableInteractor<Nothing?, Character>() {

    override fun execute(params: Nothing?) = repository.observeActiveCharacter()

}