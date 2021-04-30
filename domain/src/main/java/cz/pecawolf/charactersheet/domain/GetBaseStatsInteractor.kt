package cz.pecawolf.charactersheet.domain

import cz.pecawolf.charactersheet.data.CharacterRepository

class GetBaseStatsInteractor(private val repository: CharacterRepository) {
    fun getStats(): String {
        return repository.getCharacter()
    }
}