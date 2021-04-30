package cz.pecawolf.charactersheet.domain

import cz.pecawolf.charactersheet.common.model.BaseStatsEntity
import cz.pecawolf.charactersheet.data.CharacterRepository
import cz.pecawolf.charactersheet.domain.model.BaseStats

class SetBaseStatsInteractor(private val repository: CharacterRepository) {
    fun setStats(stats: BaseStats) {
        return repository.setCharacter(
            stats.run {
                BaseStatsEntity(
                    name,
                    species.standardName,
                    luck,
                    wounds,
                    str,
                    dex,
                    vit,
                    inl,
                    wis,
                    cha,
                    money
                )
            }
        )
    }
}