package com.pecawolf.domain.interactor

import com.pecawolf.data.CharacterRepository
import com.pecawolf.domain.CompletableInteractor

class SetActiveCharacterIdInteractor(private val repository: CharacterRepository) :
    CompletableInteractor<Long>() {

    override fun execute(characterId: Long) = repository.setActiveCharacter(characterId)
}