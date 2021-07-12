package com.pecawolf.model

data class CharacterSnippet(
    val characterId: Long,
    val name: String,
    val species: BaseStats.Species,
    val world: BaseStats.World
)