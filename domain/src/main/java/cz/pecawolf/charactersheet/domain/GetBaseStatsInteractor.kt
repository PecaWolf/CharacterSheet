package cz.pecawolf.charactersheet.domain

import cz.pecawolf.charactersheet.common.model.BaseStats
import cz.pecawolf.charactersheet.data.CharacterRepository
import io.reactivex.rxjava3.core.Single

class GetBaseStatsInteractor(private val repository: CharacterRepository) {
    fun getStats(characterId: String): Single<BaseStats> {
        return repository.getCharacter(characterId)
    }
}