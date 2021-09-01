package com.pecawolf.domain.interactor

import com.pecawolf.domain.repository.ICharacterRepository

class SetActiveCharacterIdInteractor(private val repository: ICharacterRepository) :
    CompletableInteractor<Long>() {

    override fun execute(characterId: Long) = repository.setActiveCharacterId(characterId)
}