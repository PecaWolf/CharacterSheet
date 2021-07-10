package com.pecawolf.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
class BaseStats(
    val name: String,
    val species: Species,
    val world: World,
    var luck: Int,
    var wounds: Int,
    val str: Int,
    val dex: Int,
    val vit: Int,
    val inl: Int,
    val wis: Int,
    val cha: Int
) : Parcelable, Serializable {
    val luckAndWounds: Pair<Int, Int>
        get() = luck to wounds

    val strength: String
        get() = "$str"
    val strengthTrap: String
        get() = "${str * 2}"
    val dexterity: String
        get() = "$dex"
    val dexterityTrap: String
        get() = "${dex * 2}"
    val vitality: String
        get() = "$vit"
    val vitalityTrap: String
        get() = "${vit * 2}"
    val inteligence: String
        get() = "$inl"
    val inteligenceTrap: String
        get() = "${inl * 2}"
    val wisdom: String
        get() = "$wis"
    val wisdomTrap: String
        get() = "${wis * 2}"
    val charisma: String
        get() = "$cha"
    val charismaTrap: String
        get() = "${cha * 2}"

    enum class Species {
        HUMAN,

        // last realm
        DWARF,
        ELF,
        HAVLIN,

        // dark way
        KARANTI,
        NATHOREAN,
        SEARIAN,
        GUSMERIAN,

        // cold frontier
        KRUNG;
    }

    enum class World(val species: List<Species>) {
        LAST_REALM(
            listOf(
                Species.HUMAN,
                Species.DWARF,
                Species.ELF,
                Species.HAVLIN,
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
        )
    }
}