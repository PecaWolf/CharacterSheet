package com.pecawolf.charactersheet.ext

import android.content.res.Resources
import androidx.core.content.res.ResourcesCompat
import com.pecawolf.charactersheet.R
import com.pecawolf.model.Item

fun Item.getIcon(resources: Resources) = ResourcesCompat.getDrawable(resources, getIcon(), null)

fun Item.getIcon() = when (this) {
    Item.Weapon.Melee.BareHands -> R.drawable.ic_backpack
    is Item.Weapon.Melee.Axe -> R.drawable.ic_axe
    is Item.Weapon.Melee.Hammer -> R.drawable.ic_hammer
    is Item.Weapon.Melee.Knife -> R.drawable.ic_knife
    is Item.Weapon.Melee.Sword -> R.drawable.ic_sword

    is Item.Weapon.Ranged.Bow -> R.drawable.ic_bow
    is Item.Weapon.Ranged.Crossbow -> R.drawable.ic_crossbow
    is Item.Weapon.Ranged.Pistol -> R.drawable.ic_pistol
    is Item.Weapon.Ranged.Revolver -> R.drawable.ic_revolver
    is Item.Weapon.Ranged.SubmachineGun -> R.drawable.ic_smg
    is Item.Weapon.Ranged.Rifle -> R.drawable.ic_rifle
    is Item.Weapon.Ranged.Shotgun -> R.drawable.ic_shotgun
    is Item.Weapon.Ranged.MachineGun -> R.drawable.ic_machine_gun
    is Item.Weapon.Ranged.AntimaterialGun -> R.drawable.ic_anti_material

    is Item.Weapon.Grenade -> R.drawable.ic_grenade

    Item.Armor.None -> R.drawable.ic_no_armor
    is Item.Armor.Clothes -> R.drawable.ic_clothes
    is Item.Armor.Kevlar -> R.drawable.ic_kevlar
    is Item.Armor.VacSuit -> R.drawable.ic_vac_suit
    is Item.Armor.VacArmor -> R.drawable.ic_vac_armor
    is Item.Armor.ExoSkeleton -> R.drawable.ic_exo_skeleton
    is Item.Armor.PoweredArmor -> R.drawable.ic_power_armor

    is Item.Electronics -> R.drawable.ic_electronics
    is Item.Food -> R.drawable.ic_hamburger
    is Item.Potion -> R.drawable.ic_potion
    is Item.Other -> R.drawable.ic_bag
}