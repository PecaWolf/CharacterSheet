package com.pecawolf.charactersheet.ext

import com.pecawolf.charactersheet.R
import com.pecawolf.model.BaseStats
import com.pecawolf.model.Item

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

fun Item.ItemType.getLocalizedName() = when (this) {
    Item.ItemType.BARE_HANDS -> R.string.item_type_bare_hands
    Item.ItemType.KNIFE -> R.string.item_type_knife
    Item.ItemType.SWORD -> R.string.item_type_sword
    Item.ItemType.AXE -> R.string.item_type_axe
    Item.ItemType.BOW -> R.string.item_type_bow
    Item.ItemType.CROSSBOW -> R.string.item_type_crossbow
    Item.ItemType.PISTOL -> R.string.item_type_pistol
    Item.ItemType.REVOLVER -> R.string.item_type_revolver
    Item.ItemType.RIFLE -> R.string.item_type_rifle
    Item.ItemType.SUBMACHINE_GUN -> R.string.item_type_submachine_gun
    Item.ItemType.SHOTGUN -> R.string.item_type_shotgun
    Item.ItemType.MACHINE_GUN -> R.string.item_type_machine_gun
    Item.ItemType.ANTIMATERIAL_GUN -> R.string.item_type_antimaterial_gun
    Item.ItemType.GRENADE -> R.string.item_type_grenade

    Item.ItemType.NONE -> R.string.item_type_none
    Item.ItemType.CLOTHING -> R.string.item_type_clothing
    Item.ItemType.KEVLAR -> R.string.item_type_kevlar
    Item.ItemType.EXO_SKELETON -> R.string.item_type_exo_skeleton
    Item.ItemType.VAC_SUIT -> R.string.item_type_vac_suit
    Item.ItemType.VAC_ARMOR -> R.string.item_type_vac_armor
    Item.ItemType.POWERED_ARMOR -> R.string.item_type_powered_armor

    Item.ItemType.OTHER -> R.string.item_type_other
}

fun Item.DamageType.getLocalizedName() = when (this) {
    Item.DamageType.BLUNT -> R.string.damage_type_blunt
    Item.DamageType.SLASH -> R.string.damage_type_slash
    Item.DamageType.PIERCE -> R.string.damage_type_pierce
    Item.DamageType.MAGIC -> R.string.damage_type_magic
    Item.DamageType.ELECTRICITY -> R.string.damage_type_electricity
    Item.DamageType.FIRE -> R.string.damage_type_fire
    Item.DamageType.BALLISTIC -> R.string.damage_type_ballistic
    Item.DamageType.RADIATION -> R.string.damage_type_radiation
    Item.DamageType.PARTICLE -> R.string.damage_type_particle
    Item.DamageType.BREATH -> R.string.damage_type_breath
    Item.DamageType.KINETIC -> R.string.damage_type_kinetic
}