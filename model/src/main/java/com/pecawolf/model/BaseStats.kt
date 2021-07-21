package com.pecawolf.model

import android.os.Parcelable
import com.pecawolf.model.BaseStats.Stat.Charisma
import com.pecawolf.model.BaseStats.Stat.Dexterity
import com.pecawolf.model.BaseStats.Stat.Intelligence
import com.pecawolf.model.BaseStats.Stat.Strength
import com.pecawolf.model.BaseStats.Stat.Vitality
import com.pecawolf.model.BaseStats.Stat.Wisdom
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

    sealed class Stat(open var value: Int) : Parcelable {
        @Parcelize
        data class Strength(override var value: Int) : Stat(value)

        @Parcelize
        data class Dexterity(override var value: Int) : Stat(value)

        @Parcelize
        data class Vitality(override var value: Int) : Stat(value)

        @Parcelize
        data class Intelligence(override var value: Int) : Stat(value)

        @Parcelize
        data class Wisdom(override var value: Int) : Stat(value)

        @Parcelize
        data class Charisma(override var value: Int) : Stat(value)

        val trap: Int
            get() = value * 2
    }

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