package com.pecawolf.data.mapper

import com.pecawolf.data.model.ItemData
import com.pecawolf.model.Item

class ItemDataMapper {
    fun fromData(data: ItemData): Item = data.run {
        when (Item.ItemType.valueOf(type)) {
            Item.ItemType.BARE_HANDS -> Item.Weapon.Melee.BareHands
            Item.ItemType.KNIFE -> Item.Weapon.Melee.Knife(
                itemId,
                name,
                description,
                count,
                parseEnhancements(),
                parseLoadouts(),
                parseDamage(),
                parseDamageTypes()
            )
            Item.ItemType.SWORD -> Item.Weapon.Melee.Sword(
                itemId,
                name,
                description,
                count,
                parseEnhancements(),
                parseLoadouts(),
                parseDamage(),
                Item.Weapon.Wield.valueOf(wield),
                parseDamageTypes()
            )
            Item.ItemType.AXE -> Item.Weapon.Melee.Axe(
                itemId,
                name,
                description,
                count,
                parseEnhancements(),
                parseLoadouts(),
                parseDamage(),
                Item.Weapon.Wield.valueOf(wield),
                parseDamageTypes()
            )
            Item.ItemType.HAMMER -> Item.Weapon.Melee.Hammer(
                itemId,
                name,
                description,
                count,
                parseEnhancements(),
                parseLoadouts(),
                parseDamage(),
                Item.Weapon.Wield.valueOf(wield),
                parseDamageTypes()
            )
            Item.ItemType.BOW -> Item.Weapon.Ranged.Bow(
                itemId,
                name,
                description,
                count,
                parseEnhancements(),
                parseLoadouts(),
                parseDamage(),
                parseDamageTypes(),
                magazine,
                rateOfFire,
            )
            Item.ItemType.CROSSBOW -> Item.Weapon.Ranged.Crossbow(
                itemId,
                name,
                description,
                count,
                parseEnhancements(),
                parseLoadouts(),
                parseDamage(),
                parseDamageTypes(),
                magazine,
                rateOfFire,
            )
            Item.ItemType.PISTOL -> Item.Weapon.Ranged.Pistol(
                itemId,
                name,
                description,
                count,
                parseEnhancements(),
                parseLoadouts(),
                parseDamage(),
                parseDamageTypes(),
                magazine,
                rateOfFire,
            )
            Item.ItemType.REVOLVER -> Item.Weapon.Ranged.Revolver(
                itemId,
                name,
                description,
                count,
                parseEnhancements(),
                parseLoadouts(),
                parseDamage(),
                parseDamageTypes(),
                magazine,
                rateOfFire,
            )
            Item.ItemType.RIFLE -> Item.Weapon.Ranged.Rifle(
                itemId,
                name,
                description,
                count,
                parseEnhancements(),
                parseLoadouts(),
                parseDamage(),
                parseDamageTypes(),
                magazine,
                rateOfFire,
            )
            Item.ItemType.SUBMACHINE_GUN -> Item.Weapon.Ranged.SubmachineGun(
                itemId,
                name,
                description,
                count,
                parseEnhancements(),
                parseLoadouts(),
                parseDamage(),
                parseDamageTypes(),
                magazine,
                rateOfFire,
            )
            Item.ItemType.SHOTGUN -> Item.Weapon.Ranged.Shotgun(
                itemId,
                name,
                description,
                count,
                parseEnhancements(),
                parseLoadouts(),
                parseDamage(),
                parseDamageTypes(),
                magazine,
                rateOfFire,
            )
            Item.ItemType.MACHINE_GUN -> Item.Weapon.Ranged.MachineGun(
                itemId,
                name,
                description,
                count,
                parseEnhancements(),
                parseLoadouts(),
                parseDamage(),
                parseDamageTypes(),
                magazine,
                rateOfFire,
            )
            Item.ItemType.ANTIMATERIAL_GUN -> Item.Weapon.Ranged.AntimaterialGun(
                itemId,
                name,
                description,
                count,
                parseEnhancements(),
                parseLoadouts(),
                parseDamage(),
                parseDamageTypes(),
                magazine,
                rateOfFire,
            )
            Item.ItemType.GRENADE -> Item.Weapon.Grenade(
                itemId,
                name,
                description,
                count,
                parseEnhancements(),
                parseDamage(),
                parseDamageTypes(),
            )
            Item.ItemType.NONE -> Item.Armor.None
            Item.ItemType.CLOTHING -> Item.Armor.Clothing(
                itemId,
                name,
                description,
                count,
                parseEnhancements(),
                parseDamageTypes(),
            )
            Item.ItemType.KEVLAR -> Item.Armor.Kevlar(
                itemId,
                name,
                description,
                count,
                parseEnhancements(),
                parseLoadouts(),
                parseDamage(),
                parseDamageTypes(),
            )
            Item.ItemType.EXO_SKELETON -> Item.Armor.ExoSkeleton(
                itemId,
                name,
                description,
                count,
                parseEnhancements(),
                parseLoadouts(),
                parseDamage(),
                parseDamageTypes(),
            )
            Item.ItemType.VAC_SUIT -> Item.Armor.VacSuit(
                itemId,
                name,
                description,
                count,
                parseEnhancements(),
                parseLoadouts(),
                parseDamage(),
                parseDamageTypes(),
            )
            Item.ItemType.VAC_ARMOR -> Item.Armor.VacArmor(
                itemId,
                name,
                description,
                count,
                parseEnhancements(),
                parseLoadouts(),
                parseDamage(),
                parseDamageTypes(),
            )
            Item.ItemType.POWERED_ARMOR -> Item.Armor.PoweredArmor(
                itemId,
                name,
                description,
                count,
                parseEnhancements(),
                parseLoadouts(),
                parseDamage(),
                parseDamageTypes(),
            )
            Item.ItemType.OTHER -> Item.Other(
                itemId,
                name,
                description,
                count,
                parseEnhancements(),
            )
        }
    }

    fun toData(item: Item, ownerId: Long): ItemData {
        val type = when (item) {
            is Item.Armor.Clothing -> Item.ItemType.CLOTHING
            is Item.Armor.ExoSkeleton -> Item.ItemType.EXO_SKELETON
            is Item.Armor.Kevlar -> Item.ItemType.KEVLAR
            Item.Armor.None -> Item.ItemType.NONE
            is Item.Armor.PoweredArmor -> Item.ItemType.POWERED_ARMOR
            is Item.Armor.VacArmor -> Item.ItemType.VAC_ARMOR
            is Item.Armor.VacSuit -> Item.ItemType.VAC_SUIT
            is Item.Other -> Item.ItemType.OTHER
            is Item.Weapon.Grenade -> Item.ItemType.GRENADE
            is Item.Weapon.Melee.Axe -> Item.ItemType.AXE
            Item.Weapon.Melee.BareHands -> Item.ItemType.BARE_HANDS
            is Item.Weapon.Melee.Hammer -> Item.ItemType.HAMMER
            is Item.Weapon.Melee.Knife -> Item.ItemType.KNIFE
            is Item.Weapon.Melee.Sword -> Item.ItemType.SWORD
            is Item.Weapon.Ranged.AntimaterialGun -> Item.ItemType.ANTIMATERIAL_GUN
            is Item.Weapon.Ranged.Bow -> Item.ItemType.BOW
            is Item.Weapon.Ranged.Crossbow -> Item.ItemType.CROSSBOW
            is Item.Weapon.Ranged.MachineGun -> Item.ItemType.MACHINE_GUN
            is Item.Weapon.Ranged.Pistol -> Item.ItemType.PISTOL
            is Item.Weapon.Ranged.Revolver -> Item.ItemType.REVOLVER
            is Item.Weapon.Ranged.Rifle -> Item.ItemType.RIFLE
            is Item.Weapon.Ranged.Shotgun -> Item.ItemType.SHOTGUN
            is Item.Weapon.Ranged.SubmachineGun -> Item.ItemType.SUBMACHINE_GUN
        }

        return item.run {
            ItemData(
                itemId,
                ownerId,
                type.name,
                name,
                description,
                count,
                allowedLoadouts.map { it.name },
                enhancements.map { "${it.name}$ENHANCEMENT_DELIMITER${it.description}" },
                damage.name,
                (this as? Item.Weapon)?.run { wield.name } ?: "",
                damageTypes.map { it.name },
                (this as? Item.Weapon.Ranged)?.run { magazine } ?: 0,
                (this as? Item.Weapon.Ranged)?.run { rateOfFire } ?: 0,
            )
        }
    }

    private fun ItemData.parseEnhancements() = enhancements.map {
        val (name, description) = it.split(ENHANCEMENT_DELIMITER)
        Item.Enhancement(name, description)
    }

    private fun ItemData.parseLoadouts() =
        allowedLoadouts.map { Item.LoadoutType.valueOf(it) }.toMutableList()

    private fun ItemData.parseDamage() = Item.Damage.valueOf(damage)

    private fun ItemData.parseDamageTypes() =
        damageTypes.map { Item.DamageType.valueOf(it) }.toMutableSet()

    companion object {
        private const val ENHANCEMENT_DELIMITER = ":"
    }
}