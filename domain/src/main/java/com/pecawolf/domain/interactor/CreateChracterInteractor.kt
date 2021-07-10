package com.pecawolf.domain.interactor

import com.pecawolf.data.CharacterRepository
import com.pecawolf.domain.CompletableInteractor
import com.pecawolf.model.BaseStats

class CreateChracterInteractor(private val respository: CharacterRepository) :
    CompletableInteractor<BaseStats>() {

    override fun execute(params: BaseStats) = respository.createCharacter(params)
}