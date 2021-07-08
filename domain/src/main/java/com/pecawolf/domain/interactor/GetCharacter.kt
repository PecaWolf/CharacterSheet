package com.pecawolf.domain.interactor

import com.pecawolf.data.CharacterRepository
import com.pecawolf.domain.BaseInteractor
import io.reactivex.rxjava3.core.Single

class GetCharacter(private val repository: CharacterRepository) :
    BaseInteractor<Nothing, com.pecawolf.model.Character>() {
    override fun execute(params: Nothing): Single<com.pecawolf.model.Character> {
        return repository.getActiveCharacter()
    }
}