package com.pecawolf.domain.interactor

import com.pecawolf.domain.repository.ICharacterRepository
import com.pecawolf.model.CharacterSnippet

class GetCharactersInteractor(
    private val repository: ICharacterRepository,
) : SingleInteractor<Nothing?, List<CharacterSnippet>>() {

    override fun execute(params: Nothing?) = repository.getCharacterSnippets()
}