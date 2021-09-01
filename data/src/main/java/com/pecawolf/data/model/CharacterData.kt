package com.pecawolf.data.model

data class CharacterData(
    var characterId: Long,
    var name: String,
    var species: String,
    var world: String,
    var luck: Int,
    var wounds: Int,
    var str: Int,
    var dex: Int,
    var vit: Int,
    var inl: Int,
    var wis: Int,
    var cha: Int,
    var money: Int,
    var primary: Long,
    var secondary: Long,
    var tertiary: Long,
    var clothes: Long,
    var armor: Long,
    val skills: MutableMap<String, Int> = mutableMapOf(),
) {
    companion object {
        fun new(baseStats: com.pecawolf.model.BaseStats) = CharacterData(
            0,
            baseStats.name,
            baseStats.species.name,
            baseStats.world.name,
            baseStats.luck,
            baseStats.wounds,
            baseStats.str.value,
            baseStats.dex.value,
            baseStats.vit.value,
            baseStats.inl.value,
            baseStats.wis.value,
            baseStats.cha.value,
            0,
            0L,
            0L,
            0L,
            0L,
            0L,
            mutableMapOf()
        )
    }
}