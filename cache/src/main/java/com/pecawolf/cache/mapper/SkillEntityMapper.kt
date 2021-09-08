package com.pecawolf.cache.mapper

import com.pecawolf.cache.model.SkillsEntity
import com.pecawolf.data.model.SkillsData

class SkillEntityMapper {
    fun fromEntity(entity: SkillsEntity.SkillsStatEntity) = SkillsData(
        entity.stat,
        entity.skills.map {
            SkillsData.SkillData(
                it.code,
                it.name
            )
        }
    )
}