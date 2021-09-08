package com.pecawolf.cache.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SkillsEntity(
    @Json(name = "availableSkills") val availableSkills: List<SkillsStatEntity> = listOf(),
) {
    @JsonClass(generateAdapter = true)
    data class SkillsStatEntity(
        @Json(name = "stat") val stat: String = "",
        @Json(name = "skills") val skills: List<SkillEntity> = listOf(),
    ) {
        @JsonClass(generateAdapter = true)
        data class SkillEntity(
            @Json(name = "stat") val stat: String = "",
            @Json(name = "code") val code: String = "",
            @Json(name = "name") val name: Map<String, String> = mapOf(),
        )
    }
}