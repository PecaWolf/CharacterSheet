package com.pecawolf.domain.interactor

import com.pecawolf.domain.repository.ICharacterRepository
import com.pecawolf.model.BaseStats

class CreateChracterInteractor(private val respository: ICharacterRepository) :
    CompletableInteractor<BaseStats>() {

    override fun execute(params: BaseStats) = respository.createCharacter(params)
}