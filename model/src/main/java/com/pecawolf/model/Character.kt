package com.pecawolf.model

data class Character(
    val characterId: Long,
    val baseStats: BaseStats,
    val inventory: Inventory,
    val skills: Skills,
) {
    companion object {
        fun new(baseStats: BaseStats) = Character(0, baseStats, Inventory(), Skills())
    }
}