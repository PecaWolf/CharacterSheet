package com.pecawolf.domain.interactor

import com.pecawolf.data.CharacterRepository
import com.pecawolf.model.CharacterSnippet

class GetCharactersInteractor(
    private val repository: CharacterRepository
) : SingleInteractor<Nothing?, List<CharacterSnippet>>() {

    override fun execute(params: Nothing?) = repository.getCharacterSnippets()
}