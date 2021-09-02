package com.pecawolf.model

class BaseStats(
    var name: String,
    val species: Species,
    val world: World,
    var luck: Int,
    var wounds: Int,
    var str: Rollable.Stat.Strength,
    var dex: Rollable.Stat.Dexterity,
    var vit: Rollable.Stat.Vitality,
    var inl: Rollable.Stat.Intelligence,
    var wis: Rollable.Stat.Wisdom,
    var cha: Rollable.Stat.Charisma,
) {
    val luckAndWounds: Pair<Int, Int>
        get() = luck to wounds

    enum class Species {
        HUMAN,
        DWARF,
        ELF,
        HAVLIN,
        GNOME,
        KARANTI,
        NATHOREAN,
        SEARIAN,
        GUSMERIAN,
        KRUNG;
    }

    enum class World(val species: List<Species>) {
        BRAVE_NEW_WORLD(
            listOf(
                Species.HUMAN,
                Species.DWARF,
                Species.ELF,
                Species.HAVLIN,
            )
        ),
        BLACKWOOD_VALE(
            listOf(
                Species.HUMAN,
                Species.DWARF,
                Species.ELF,
                Species.GNOME,
            )
        ),
        BLUE_WAY(
            listOf(
                Species.HUMAN,
                Species.KARANTI,
                Species.NATHOREAN,
                Species.SEARIAN,
                Species.GUSMERIAN,
            )
        ),
        DARK_WAY(
            listOf(
                Species.HUMAN,
                Species.KARANTI,
                Species.NATHOREAN,
                Species.SEARIAN,
                Species.GUSMERIAN,
            )
        ),
        COLD_FRONTIER(
            listOf(
                Species.HUMAN,
                Species.KRUNG
            )
        );
    }
}