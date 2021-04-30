package cz.pecawolf.charactersheet.domain

import cz.pecawolf.charactersheet.common.model.BaseStats
import cz.pecawolf.charactersheet.data.CharacterRepository

class SetBaseStatsInteractor(private val repository: CharacterRepository) {
    fun setStats(stats: BaseStats) {
        return repository.setCharacter(
            stats
        )
    }
}