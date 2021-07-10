package com.pecawolf.charactersheet.ext

import com.pecawolf.charactersheet.R
import com.pecawolf.model.BaseStats

fun BaseStats.World.getLocalizedName() = when (this) {
    BaseStats.World.LAST_REALM -> R.string.world_last_realm
    BaseStats.World.BLUE_WAY -> R.string.world_blue_way
    BaseStats.World.DARK_WAY -> R.string.world_dark_way
    BaseStats.World.COLD_FRONTIER -> R.string.world_cold_frontier
}

fun BaseStats.Species.getLocalizedName() = when (this) {
    BaseStats.Species.HUMAN -> R.string.species_human
    BaseStats.Species.DWARF -> R.string.species_dwarf
    BaseStats.Species.ELF -> R.string.species_elf
    BaseStats.Species.HAVLIN -> R.string.species_havlin
    BaseStats.Species.KARANTI -> R.string.species_karanti
    BaseStats.Species.NATHOREAN -> R.string.species_nathorean
    BaseStats.Species.SEARIAN -> R.string.species_searian
    BaseStats.Species.GUSMERIAN -> R.string.species_gusmerian
    BaseStats.Species.KRUNG -> R.string.species_krung
}