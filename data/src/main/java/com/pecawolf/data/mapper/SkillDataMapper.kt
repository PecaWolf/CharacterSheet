package com.pecawolf.data.mapper

import com.pecawolf.data.model.SkillsData
import com.pecawolf.model.Rollable
import com.pecawolf.model.Rollable.Skill

class SkillDataMapper {
    fun fromData(data: SkillsData.SkillData, usedStat: Rollable.Stat, locale: String, value: Int) = Skill(
        data.code,
        data.name[locale] ?: "code:${data.code}",
        usedStat,
        value
    )
}
