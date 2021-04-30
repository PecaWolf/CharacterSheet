package cz.pecawolf.charactersheet.domain.model

import kotlin.experimental.and

data class BaseStats(
    val name: String,
    val species: Species,
    var luck: Int,
    var wounds: Int,
    val str: Int,
    val dex: Int,
    val vit: Int,
    val inl: Int,
    val wis: Int,
    val cha: Int,
    val money: Int
) {

    val luckAndWounds: String
        get() = "$luck + $wounds"

    val strength: String
        get() = "${str}"
    val strengthTrap: String
        get() = "${str * 2}"
    val dexterity: String
        get() = "${dex}"
    val dexterityTrap: String
        get() = "${dex * 2}"
    val vitality: String
        get() = "${vit}"
    val vitalityTrap: String
        get() = "${vit * 2}"
    val inteligence: String
        get() = "${inl}"
    val inteligenceTrap: String
        get() = "${inl * 2}"
    val wisdom: String
        get() = "${wis}"
    val wisdomTrap: String
        get() = "${wis * 2}"
    val charisma: String
        get() = "${cha}"
    val charismaTrap: String
        get() = "${cha * 2}"

    enum class Species(
        val standardName: String,
        val mask: Byte
    ) {
        HUMAN(
            "Human",
            ALL
        ),

        // last realm
        DWARF(
            "Dwarf",
            LAST_REALM
        ),
        ELF(
            "Elf",
            LAST_REALM
        ),
        HAVLIN(
            "Havlin",
            LAST_REALM
        ),

        // dark way
        KARANTI(
            "Karanti",
            DARK_WAY
        ),
        NATHOREAN(
            "Nathorean",
            DARK_WAY
        ),
        SEARIAN(
            "Searian",
            DARK_WAY
        ),
        GUSMERIAN(
            "Gusmerian",
            DARK_WAY
        ),

        // cold frontier
        KRUNG(
            "Krung",
            COLD_FRONTIER
        );

        val isLastRealm: Boolean
            get() = mask.and(LAST_REALM) == LAST_REALM

        val isDarkWay: Boolean
            get() = mask.and(DARK_WAY) == DARK_WAY

        val isColdFrontier: Boolean
            get() = mask.and(COLD_FRONTIER) == COLD_FRONTIER
    }

    companion object {
        private const val LAST_REALM: Byte = 1
        private const val DARK_WAY: Byte = 2
        private const val COLD_FRONTIER: Byte = 4
        private const val ALL: Byte = 7
    }
}