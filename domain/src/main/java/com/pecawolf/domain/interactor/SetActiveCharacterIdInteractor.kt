package com.pecawolf.domain.interactor

import com.pecawolf.data.CharacterRepository

class SetActiveCharacterIdInteractor(private val repository: CharacterRepository) :
    CompletableInteractor<Long>() {

    override fun execute(characterId: Long) = repository.setActiveCharacterId(characterId)
}