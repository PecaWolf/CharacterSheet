package com.pecawolf.data.model

data class SkillsData(
    val stat: String = "",
    val skills: List<SkillData> = listOf(),
) {
    data class SkillData(
        val code: String = "",
        val name: Map<String, String> = mapOf(),
        val value: Int = 0,
    )
}