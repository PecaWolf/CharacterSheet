package cz.pecawolf.charactersheet.common.model

import cz.pecawolf.charactersheet.common.Rules
import kotlin.experimental.and

class BaseStats(
    val name: String,
    val species: Species,
    var luck: Int,
    var wounds: Int,
    val str: CharacterStat,
    val dex: CharacterStat,
    val vit: CharacterStat,
    val inl: CharacterStat,
    val wis: CharacterStat,
    val cha: CharacterStat,
    val money: Int
) {

    val luckAndWounds: String
        get() = "$luck + $wounds"

    val strength: String
        get() = "${str.value}"
    val strengthTrap: String
        get() = "${str.trap}"
    val dexterity: String
        get() = "${dex.value}"
    val dexterityTrap: String
        get() = "${dex.trap}"
    val vitality: String
        get() = "${vit.value}"
    val vitalityTrap: String
        get() = "${vit.trap}"
    val inteligence: String
        get() = "${inl.value}"
    val inteligenceTrap: String
        get() = "${inl.trap}"
    val wisdom: String
        get() = "${wis.value}"
    val wisdomTrap: String
        get() = "${wis.trap}"
    val charisma: String
        get() = "${cha.value}"
    val charismaTrap: String
        get() = "${cha.trap}"

    data class CharacterStat(val value: Int) {
        val trap: Int
            get() = Rules.getCharacterStatTrap(value)
    }

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

        companion object {
            fun fromName(name: String): Species {
                return values().firstOrNull { it.standardName == name }
                    ?: throw IllegalArgumentException("Species name \'$name\' not found")
            }
        }
    }

    companion object {
        private const val LAST_REALM: Byte = 1
        private const val DARK_WAY: Byte = 2
        private const val COLD_FRONTIER: Byte = 4
        private const val ALL: Byte = 7
    }
}