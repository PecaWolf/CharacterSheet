package com.pecawolf.model

data class Skills(
    val strength: List<Rollable.Skill> = listOf(),
    val dexterity: List<Rollable.Skill> = listOf(),
    val vitality: List<Rollable.Skill> = listOf(),
    val intelligence: List<Rollable.Skill> = listOf(),
    val wisdom: List<Rollable.Skill> = listOf(),
    val charisma: List<Rollable.Skill> = listOf(),
)