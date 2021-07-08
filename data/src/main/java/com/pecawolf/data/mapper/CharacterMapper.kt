package com.pecawolf.data.mapper

import com.pecawolf.cache.model.CharacterEntity
import com.pecawolf.remote.model.CharacterResponse

class CharacterMapper(
    private val baseStatsMapper: BaseStatsMapper,
    private val equipmentMapper: EquipmentMapper
) : BaseMapper<com.pecawolf.model.Character, CharacterResponse, CharacterEntity> {
    override fun fromResponse(response: CharacterResponse) = response.run {
        com.pecawolf.model.Character(
            baseStatsMapper.fromResponse(base),
            equipmentMapper.fromResponse(equipment)
        )
    }

    override fun fromEntity(entity: CharacterEntity) = entity.run {
        com.pecawolf.model.Character(
            baseStatsMapper.fromEntity(base),
            equipmentMapper.fromEntity(equipment)
        )
    }

    override fun toEntity(model: com.pecawolf.model.Character) = model.run {
        CharacterEntity(
            baseStatsMapper.toEntity(base),
            equipmentMapper.toEntity(equipment)
        )
    }
}