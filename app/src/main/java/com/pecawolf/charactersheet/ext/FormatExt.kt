package com.pecawolf.charactersheet.ext

import android.content.Context
import com.pecawolf.charactersheet.R
import java.text.NumberFormat

fun Int.formatAmount(): String = NumberFormat.getIntegerInstance().format(this)

fun com.pecawolf.model.BaseStats.World.getLocalizedName(context: Context) =
    context.getString(getLocalizedName())

fun com.pecawolf.model.BaseStats.World.getLocalizedName() = when (this) {
    com.pecawolf.model.BaseStats.World.LAST_REALM -> R.string.world_last_realm
    com.pecawolf.model.BaseStats.World.BLUE_WAY -> R.string.world_blue_way
    com.pecawolf.model.BaseStats.World.DARK_WAY -> R.string.world_dark_way
    com.pecawolf.model.BaseStats.World.COLD_FRONTIER -> R.string.world_cold_frontier
}

fun com.pecawolf.model.BaseStats.Species.getLocalizedName(context: Context) =
    context.getString(getLocalizedName())

fun com.pecawolf.model.BaseStats.Species.getLocalizedName() = when (this) {
    com.pecawolf.model.BaseStats.Species.HUMAN -> R.string.species_human
    com.pecawolf.model.BaseStats.Species.DWARF -> R.string.species_dwarf
    com.pecawolf.model.BaseStats.Species.ELF -> R.string.species_elf
    com.pecawolf.model.BaseStats.Species.HAVLIN -> R.string.species_havlin
    com.pecawolf.model.BaseStats.Species.KARANTI -> R.string.species_karanti
    com.pecawolf.model.BaseStats.Species.NATHOREAN -> R.string.species_nathorean
    com.pecawolf.model.BaseStats.Species.SEARIAN -> R.string.species_searian
    com.pecawolf.model.BaseStats.Species.GUSMERIAN -> R.string.species_gusmerian
    com.pecawolf.model.BaseStats.Species.KRUNG -> R.string.species_krung
}

fun com.pecawolf.model.Item.ItemType.getLocalizedName(context: Context) =
    context.getString(getLocalizedName())

fun com.pecawolf.model.Item.ItemType.getLocalizedName() = when (this) {
    com.pecawolf.model.Item.ItemType.BARE_HANDS -> R.string.item_type_bare_hands
    com.pecawolf.model.Item.ItemType.KNIFE -> R.string.item_type_knife
    com.pecawolf.model.Item.ItemType.SWORD -> R.string.item_type_sword
    com.pecawolf.model.Item.ItemType.AXE -> R.string.item_type_axe
    com.pecawolf.model.Item.ItemType.HAMMER -> R.string.item_type_hammer
    com.pecawolf.model.Item.ItemType.BOW -> R.string.item_type_bow
    com.pecawolf.model.Item.ItemType.CROSSBOW -> R.string.item_type_crossbow
    com.pecawolf.model.Item.ItemType.PISTOL -> R.string.item_type_pistol
    com.pecawolf.model.Item.ItemType.REVOLVER -> R.string.item_type_revolver
    com.pecawolf.model.Item.ItemType.RIFLE -> R.string.item_type_rifle
    com.pecawolf.model.Item.ItemType.SUBMACHINE_GUN -> R.string.item_type_submachine_gun
    com.pecawolf.model.Item.ItemType.SHOTGUN -> R.string.item_type_shotgun
    com.pecawolf.model.Item.ItemType.MACHINE_GUN -> R.string.item_type_machine_gun
    com.pecawolf.model.Item.ItemType.ANTIMATERIAL_GUN -> R.string.item_type_antimaterial_gun
    com.pecawolf.model.Item.ItemType.GRENADE -> R.string.item_type_grenade

    com.pecawolf.model.Item.ItemType.NONE -> R.string.item_type_none
    com.pecawolf.model.Item.ItemType.CLOTHING -> R.string.item_type_clothing
    com.pecawolf.model.Item.ItemType.KEVLAR -> R.string.item_type_kevlar
    com.pecawolf.model.Item.ItemType.EXO_SKELETON -> R.string.item_type_exo_skeleton
    com.pecawolf.model.Item.ItemType.VAC_SUIT -> R.string.item_type_vac_suit
    com.pecawolf.model.Item.ItemType.VAC_ARMOR -> R.string.item_type_vac_armor
    com.pecawolf.model.Item.ItemType.POWERED_ARMOR -> R.string.item_type_powered_armor

    com.pecawolf.model.Item.ItemType.OTHER -> R.string.item_type_other
}

fun com.pecawolf.model.Item.DamageType.getLocalizedName(context: Context) =
    context.getString(getLocalizedName())

fun com.pecawolf.model.Item.DamageType.getLocalizedName() = when (this) {
    com.pecawolf.model.Item.DamageType.BLUNT -> R.string.damage_type_blunt
    com.pecawolf.model.Item.DamageType.SLASH -> R.string.damage_type_slash
    com.pecawolf.model.Item.DamageType.PIERCE -> R.string.damage_type_pierce
    com.pecawolf.model.Item.DamageType.MAGIC -> R.string.damage_type_magic
    com.pecawolf.model.Item.DamageType.ELECTRICITY -> R.string.damage_type_electricity
    com.pecawolf.model.Item.DamageType.FIRE -> R.string.damage_type_fire
    com.pecawolf.model.Item.DamageType.BALLISTIC -> R.string.damage_type_ballistic
    com.pecawolf.model.Item.DamageType.RADIATION -> R.string.damage_type_radiation
    com.pecawolf.model.Item.DamageType.PARTICLE -> R.string.damage_type_particle
    com.pecawolf.model.Item.DamageType.BREATH -> R.string.damage_type_breath
    com.pecawolf.model.Item.DamageType.KINETIC -> R.string.damage_type_kinetic
}

fun com.pecawolf.model.Item.Damage.getLocalizedName(context: Context) =
    context.getString(getLocalizedName())

fun com.pecawolf.model.Item.Damage.getLocalizedName() = when (this) {
    com.pecawolf.model.Item.Damage.NONE -> R.string.damage_none
    com.pecawolf.model.Item.Damage.LIGHT -> R.string.damage_light
    com.pecawolf.model.Item.Damage.MEDIUM -> R.string.damage_medium
    com.pecawolf.model.Item.Damage.HEAVY -> R.string.damage_heavy
}

fun com.pecawolf.model.Item.Weapon.Wield.getLocalizedName(context: Context) =
    context.getString(getLocalizedName())

fun com.pecawolf.model.Item.Weapon.Wield.getLocalizedName() = when (this) {
    com.pecawolf.model.Item.Weapon.Wield.ONE_HANDED -> R.string.wield_one_handed
    com.pecawolf.model.Item.Weapon.Wield.TWO_HANDED -> R.string.wield_two_handed
    com.pecawolf.model.Item.Weapon.Wield.MOUNTED -> R.string.wield_mounted
//    Item.Weapon.Wield.DRONE -> R.string.wield_drone
}

fun com.pecawolf.model.Item.LoadoutType.getLocalizedName(context: Context) =
    context.getString(getLocalizedName())

fun com.pecawolf.model.Item.LoadoutType.getLocalizedName() = when (this) {
    com.pecawolf.model.Item.LoadoutType.COMBAT -> R.string.new_item_loadout_combat
    com.pecawolf.model.Item.LoadoutType.SOCIAL -> R.string.new_item_loadout_social
    com.pecawolf.model.Item.LoadoutType.TRAVEL -> R.string.new_item_loadout_travel
}

fun com.pecawolf.model.Item.Slot.getLocalizedName(context: Context) =
    context.getString(getLocalizedName())

fun com.pecawolf.model.Item.Slot.getLocalizedName() = when (this) {
    com.pecawolf.model.Item.Slot.PRIMARY -> R.string.equip_slot_primary
    com.pecawolf.model.Item.Slot.SECONDARY -> R.string.equip_slot_secondary
    com.pecawolf.model.Item.Slot.TERTIARY -> R.string.equip_slot_tertiary
//    Item.Slot.GRENADE -> R.string.equip_slot_grenade
    com.pecawolf.model.Item.Slot.ARMOR -> R.string.equip_slot_armor
    com.pecawolf.model.Item.Slot.CLOTHING -> R.string.equip_slot_clothing
}

fun com.pecawolf.model.Rollable.getName(context: Context) = when (this) {
    is com.pecawolf.model.Rollable.Stat -> getLocalizedName(context)
    is com.pecawolf.model.Rollable.Skill -> name
}

fun com.pecawolf.model.Rollable.Stat.getLocalizedName(context: Context) =
    context.getString(getLocalizedName())

fun com.pecawolf.model.Rollable.Stat.getLocalizedName() = when (this) {
    is com.pecawolf.model.Rollable.Stat.Strength -> R.string.basic_info_str
    is com.pecawolf.model.Rollable.Stat.Dexterity -> R.string.basic_info_dex
    is com.pecawolf.model.Rollable.Stat.Vitality -> R.string.basic_info_vit
    is com.pecawolf.model.Rollable.Stat.Intelligence -> R.string.basic_info_int
    is com.pecawolf.model.Rollable.Stat.Wisdom -> R.string.basic_info_wis
    is com.pecawolf.model.Rollable.Stat.Charisma -> R.string.basic_info_cha
}

fun com.pecawolf.model.RollResult.getLocalizedName(context: Context) =
    context.getString(getLocalizedName())

fun com.pecawolf.model.RollResult.getLocalizedName() = when (this) {
    com.pecawolf.model.RollResult.CriticalFailure -> R.string.roll_result_failure_critical
    com.pecawolf.model.RollResult.CriticalSuccess -> R.string.roll_result_success_critical
    is com.pecawolf.model.RollResult.Failure -> R.string.roll_result_failure
    is com.pecawolf.model.RollResult.Success -> R.string.roll_result_success
}