package com.pecawolf.model

import android.os.Parcelable
import com.pecawolf.model.Rollable.Stat.Charisma
import com.pecawolf.model.Rollable.Stat.Dexterity
import com.pecawolf.model.Rollable.Stat.Intelligence
import com.pecawolf.model.Rollable.Stat.Strength
import com.pecawolf.model.Rollable.Stat.Vitality
import com.pecawolf.model.Rollable.Stat.Wisdom
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
class BaseStats(
    var name: String,
    val species: Species,
    val world: World,
    var luck: Int,
    var wounds: Int,
    var str: Strength,
    var dex: Dexterity,
    var vit: Vitality,
    var inl: Intelligence,
    var wis: Wisdom,
    var cha: Charisma
) : Parcelable, Serializable {
    val luckAndWounds: Pair<Int, Int>
        get() = luck to wounds

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