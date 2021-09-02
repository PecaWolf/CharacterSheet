package com.pecawolf.remote.mapper

import com.pecawolf.data.model.SkillsData
import com.pecawolf.remote.model.SkillsResponse

class SkillsResponseMapper {
    fun fromResponse(response: SkillsResponse): SkillsData = response.run {
        SkillsData(
            stat,
            skills.map { skill ->
                SkillsData.SkillData(
                    skill.code,
                    skill.name
                )
            }
        )
    }
}