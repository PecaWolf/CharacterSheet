package com.pecawolf.data.mapper

import com.pecawolf.cache.model.BaseStatsEntity
import com.pecawolf.remote.model.BaseStatsResponse

class BaseStatsMapper :
    BaseMapper<com.pecawolf.model.BaseStats, BaseStatsResponse, BaseStatsEntity> {
    override fun fromResponse(response: BaseStatsResponse) = response.run {
        com.pecawolf.model.BaseStats(
            name,
            com.pecawolf.model.BaseStats.Species.valueOf(species),
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

    override fun fromEntity(entity: BaseStatsEntity) = entity.run {
        com.pecawolf.model.BaseStats(
            name,
            com.pecawolf.model.BaseStats.Species.valueOf(species),
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

    override fun toEntity(model: com.pecawolf.model.BaseStats) = model.run {
        BaseStatsEntity(
            name,
            species.name,
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
}