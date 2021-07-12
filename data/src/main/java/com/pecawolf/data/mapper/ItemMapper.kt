package com.pecawolf.data.mapper

import com.pecawolf.cache.model.ItemEntity
import com.pecawolf.model.Item
import com.pecawolf.model.Item.Armor.None.protections
import com.pecawolf.model.Item.Weapon.Melee.BareHands.damage
import com.pecawolf.model.Item.Weapon.Melee.BareHands.damageTypes
import com.pecawolf.model.Item.Weapon.Melee.BareHands.wield

class ItemMapper : BaseMapper<Item, ItemEntity, Long, Nothing> {

    override fun fromEntity(entity: ItemEntity, additional: List<Nothing>) = entity.run {
        when (type) {
            ItemEntity.ItemType.BARE_HANDS -> Item.Weapon.Melee.BareHands
            ItemEntity.ItemType.KNIFE -> Item.Weapon.Melee.Knife(
                itemId,
                name,
                description,
                parseEnhancements(enhancements),
                parseLoadouts(allowedLoadouts),
                parseDamage(damage!!),
                parseDamageTypes(damageTypes!!)
            )
            ItemEntity.ItemType.SWORD -> Item.Weapon.Melee.Sword(
                itemId,
                name,
                description,
                parseEnhancements(enhancements),
                parseLoadouts(allowedLoadouts),
                parseDamage(damage!!),
                parseWield(wield!!),
                parseDamageTypes(damageTypes!!)
            )
            ItemEntity.ItemType.AXE -> Item.Weapon.Melee.Axe(
                itemId,
                name,
                description,
                parseEnhancements(enhancements),
                parseLoadouts(allowedLoadouts),
                parseDamage(damage!!),
                parseWield(wield!!),
                parseDamageTypes(damageTypes!!)
            )
            ItemEntity.ItemType.BOW -> Item.Weapon.Ranged.Bow(
                itemId,
                name,
                description,
                parseEnhancements(enhancements),
                parseLoadouts(allowedLoadouts),
                parseDamage(damage!!),
                parseDamageTypes(damageTypes!!),
                magazine!!,
                rateOfFire!!
            )
            ItemEntity.ItemType.CROSSBOW -> Item.Weapon.Ranged.Crossbow(
                itemId,
                name,
                description,
                parseEnhancements(enhancements),
                parseLoadouts(allowedLoadouts),
                parseDamage(damage!!),
                parseDamageTypes(damageTypes!!),
                magazine!!,
                rateOfFire!!
            )
            ItemEntity.ItemType.PISTOL -> Item.Weapon.Ranged.Pistol(
                itemId,
                name,
                description,
                parseEnhancements(enhancements),
                parseLoadouts(allowedLoadouts),
                parseDamage(damage!!),
                parseDamageTypes(damageTypes!!),
                magazine!!,
                rateOfFire!!
            )
            ItemEntity.ItemType.REVOLVER -> Item.Weapon.Ranged.Revolver(
                itemId,
                name,
                description,
                parseEnhancements(enhancements),
                parseLoadouts(allowedLoadouts),
                parseDamage(damage!!),
                parseDamageTypes(damageTypes!!),
                magazine!!,
                rateOfFire!!
            )
            ItemEntity.ItemType.RIFLE -> Item.Weapon.Ranged.Rifle(
                itemId,
                name,
                description,
                parseEnhancements(enhancements),
                parseLoadouts(allowedLoadouts),
                parseDamage(damage!!),
                parseDamageTypes(damageTypes!!),
                magazine!!,
                rateOfFire!!
            )
            ItemEntity.ItemType.SUBMACHINE_GUN -> Item.Weapon.Ranged.SubmachineGun(
                itemId,
                name,
                description,
                parseEnhancements(enhancements),
                parseLoadouts(allowedLoadouts),
                parseDamage(damage!!),
                parseDamageTypes(damageTypes!!),
                magazine!!,
                rateOfFire!!
            )
            ItemEntity.ItemType.SHOTGUN -> Item.Weapon.Ranged.Shotgun(
                itemId,
                name,
                description,
                parseEnhancements(enhancements),
                parseLoadouts(allowedLoadouts),
                parseDamage(damage!!),
                parseDamageTypes(damageTypes!!),
                magazine!!,
                rateOfFire!!
            )
            ItemEntity.ItemType.MACHINE_GUN -> Item.Weapon.Ranged.MachineGun(
                itemId,
                name,
                description,
                parseEnhancements(enhancements),
                parseLoadouts(allowedLoadouts),
                parseDamage(damage!!),
                parseDamageTypes(damageTypes!!),
                magazine!!,
                rateOfFire!!
            )
            ItemEntity.ItemType.ANTIMATERIAL_GUN -> Item.Weapon.Ranged.AntimaterialGun(
                itemId,
                name,
                description,
                parseEnhancements(enhancements),
                parseLoadouts(allowedLoadouts),
                parseDamage(damage!!),
                parseDamageTypes(damageTypes!!),
                magazine!!,
                rateOfFire!!
            )
            ItemEntity.ItemType.NONE -> Item.Armor.None
            ItemEntity.ItemType.CLOTHING -> Item.Armor.Clothing(
                itemId,
                name,
                description,
                parseEnhancements(enhancements),
                parseDamageTypes(protections!!)
            )
            ItemEntity.ItemType.KEVLAR -> Item.Armor.Kevlar(
                itemId,
                name,
                description,
                parseEnhancements(enhancements),
                parseLoadouts(allowedLoadouts),
                parseDamageTypes(protections!!)
            )
            ItemEntity.ItemType.EXO_SKELETON -> Item.Armor.ExoSkeleton(
                itemId,
                name,
                description,
                parseEnhancements(enhancements),
                parseLoadouts(allowedLoadouts),
                parseDamageTypes(protections!!)
            )
            ItemEntity.ItemType.VAC_SUIT -> Item.Armor.VacSuit(
                itemId,
                name,
                description,
                parseEnhancements(enhancements),
                parseLoadouts(allowedLoadouts),
                parseDamageTypes(protections!!)
            )
            ItemEntity.ItemType.VAC_ARMOR -> Item.Armor.VacArmor(
                itemId,
                name,
                description,
                parseEnhancements(enhancements),
                parseLoadouts(allowedLoadouts),
                parseDamageTypes(protections!!)
            )
            ItemEntity.ItemType.POWERED_ARMOR -> Item.Armor.PoweredArmor(
                itemId,
                name,
                description,
                parseEnhancements(enhancements),
                parseLoadouts(allowedLoadouts),
                parseDamageTypes(protections!!)
            )
            ItemEntity.ItemType.OTHER -> Item.Other(
                itemId,
                name,
                description,
                parseEnhancements(enhancements)
            )
        }
    }

    override fun toEntity(model: Item, additional: List<Long>) = model.run {
        val ownerId = additional[0]

        when (this) {
            is Item.Weapon.Melee.BareHands -> ItemEntity(
                itemId,
                ownerId,
                ItemEntity.ItemType.BARE_HANDS,
                name,
                description,
                allowedLoadouts.map { it.name },
                codeEnhancements(enhancements),
                damage.name,
                wield.name,
                damageTypes.map { it.name },
                null,
                null,
                null
            )
            is Item.Weapon.Melee.Knife -> ItemEntity(
                itemId,
                ownerId,
                ItemEntity.ItemType.KNIFE,
                name,
                description,
                allowedLoadouts.map { it.name },
                codeEnhancements(enhancements),
                damage.name,
                wield.name,
                damageTypes.map { it.name },
                null,
                null,
                null
            )
            is Item.Weapon.Melee.Sword -> ItemEntity(
                itemId,
                ownerId,
                ItemEntity.ItemType.SWORD,
                name,
                description,
                allowedLoadouts.map { it.name },
                codeEnhancements(enhancements),
                damage.name,
                wield.name,
                damageTypes.map { it.name },
                null,
                null,
                null
            )
            is Item.Weapon.Melee.Axe -> ItemEntity(
                itemId,
                ownerId,
                ItemEntity.ItemType.AXE,
                name,
                description,
                allowedLoadouts.map { it.name },
                codeEnhancements(enhancements),
                damage.name,
                wield.name,
                damageTypes.map { it.name },
                null,
                null,
                null
            )
            is Item.Weapon.Ranged.Bow -> ItemEntity(
                itemId,
                ownerId,
                ItemEntity.ItemType.BOW,
                name,
                description,
                allowedLoadouts.map { it.name },
                codeEnhancements(enhancements),
                damage.name,
                wield.name,
                damageTypes.map { it.name },
                magazine,
                rateOfFire,
                null
            )
            is Item.Weapon.Ranged.Crossbow -> ItemEntity(
                itemId,
                ownerId,
                ItemEntity.ItemType.CROSSBOW,
                name,
                description,
                allowedLoadouts.map { it.name },
                codeEnhancements(enhancements),
                damage.name,
                wield.name,
                damageTypes.map { it.name },
                magazine,
                rateOfFire,
                null
            )
            is Item.Weapon.Ranged.Pistol -> ItemEntity(
                itemId,
                ownerId,
                ItemEntity.ItemType.PISTOL,
                name,
                description,
                allowedLoadouts.map { it.name },
                codeEnhancements(enhancements),
                damage.name,
                wield.name,
                damageTypes.map { it.name },
                magazine,
                rateOfFire,
                null
            )
            is Item.Weapon.Ranged.Revolver -> ItemEntity(
                itemId,
                ownerId,
                ItemEntity.ItemType.REVOLVER,
                name,
                description,
                allowedLoadouts.map { it.name },
                codeEnhancements(enhancements),
                damage.name,
                wield.name,
                damageTypes.map { it.name },
                magazine,
                rateOfFire,
                null
            )
            is Item.Weapon.Ranged.Rifle -> ItemEntity(
                itemId,
                ownerId,
                ItemEntity.ItemType.RIFLE,
                name,
                description,
                allowedLoadouts.map { it.name },
                codeEnhancements(enhancements),
                damage.name,
                wield.name,
                damageTypes.map { it.name },
                magazine,
                rateOfFire,
                null
            )
            is Item.Weapon.Ranged.SubmachineGun -> ItemEntity(
                itemId,
                ownerId,
                ItemEntity.ItemType.SUBMACHINE_GUN,
                name,
                description,
                allowedLoadouts.map { it.name },
                codeEnhancements(enhancements),
                damage.name,
                wield.name,
                damageTypes.map { it.name },
                magazine,
                rateOfFire,
                null
            )
            is Item.Weapon.Ranged.Shotgun -> ItemEntity(
                itemId,
                ownerId,
                ItemEntity.ItemType.SHOTGUN,
                name,
                description,
                allowedLoadouts.map { it.name },
                codeEnhancements(enhancements),
                damage.name,
                wield.name,
                damageTypes.map { it.name },
                magazine,
                rateOfFire,
                null
            )
            is Item.Weapon.Ranged.MachineGun -> ItemEntity(
                itemId,
                ownerId,
                ItemEntity.ItemType.MACHINE_GUN,
                name,
                description,
                allowedLoadouts.map { it.name },
                codeEnhancements(enhancements),
                damage.name,
                wield.name,
                damageTypes.map { it.name },
                magazine,
                rateOfFire,
                null
            )
            is Item.Weapon.Ranged.AntimaterialGun -> ItemEntity(
                itemId,
                ownerId,
                ItemEntity.ItemType.ANTIMATERIAL_GUN,
                name,
                description,
                allowedLoadouts.map { it.name },
                codeEnhancements(enhancements),
                damage.name,
                wield.name,
                damageTypes.map { it.name },
                magazine,
                rateOfFire,
                null
            )
            is Item.Armor.None -> ItemEntity(
                itemId,
                ownerId,
                ItemEntity.ItemType.NONE,
                name,
                description,
                allowedLoadouts.map { it.name },
                listOf(),
                null,
                null,
                null,
                null,
                null,
                protections.map { it.name }
            )
            is Item.Armor.Clothing -> ItemEntity(
                itemId,
                ownerId,
                ItemEntity.ItemType.CLOTHING,
                name,
                description,
                allowedLoadouts.map { it.name },
                codeEnhancements(enhancements),
                null,
                null,
                null,
                null,
                null,
                protections.map { it.name }
            )
            is Item.Armor.Kevlar -> ItemEntity(
                itemId,
                ownerId,
                ItemEntity.ItemType.KEVLAR,
                name,
                description,
                allowedLoadouts.map { it.name },
                codeEnhancements(enhancements),
                null,
                null,
                null,
                null,
                null,
                protections.map { it.name }
            )
            is Item.Armor.ExoSkeleton -> ItemEntity(
                itemId,
                ownerId,
                ItemEntity.ItemType.EXO_SKELETON,
                name,
                description,
                allowedLoadouts.map { it.name },
                codeEnhancements(enhancements),
                null,
                null,
                null,
                null,
                null,
                protections.map { it.name }
            )
            is Item.Armor.VacSuit -> ItemEntity(
                itemId,
                ownerId,
                ItemEntity.ItemType.VAC_SUIT,
                name,
                description,
                allowedLoadouts.map { it.name },
                codeEnhancements(enhancements),
                null,
                null,
                null,
                null,
                null,
                protections.map { it.name }
            )
            is Item.Armor.VacArmor -> ItemEntity(
                itemId,
                ownerId,
                ItemEntity.ItemType.VAC_ARMOR,
                name,
                description,
                allowedLoadouts.map { it.name },
                codeEnhancements(enhancements),
                null,
                null,
                null,
                null,
                null,
                protections.map { it.name }
            )
            is Item.Armor.PoweredArmor -> ItemEntity(
                itemId,
                ownerId,
                ItemEntity.ItemType.POWERED_ARMOR,
                name,
                description,
                allowedLoadouts.map { it.name },
                codeEnhancements(enhancements),
                null,
                null,
                null,
                null,
                null,
                protections.map { it.name }
            )
            is Item.Other -> ItemEntity(
                itemId,
                ownerId,
                ItemEntity.ItemType.OTHER,
                name,
                description,
                allowedLoadouts.map { it.name },
                codeEnhancements(enhancements),
                null,
                null,
                null,
                null,
                null,
                null
            )
        }
    }

    private fun parseEnhancements(enhancements: List<String>) = enhancements.map {
        it.split(SEPARATOR_ENHANCEMENT).run { Item.Enhancement(get(0), get(1)) }
    }

    private fun codeEnhancements(enhancements: List<Item.Enhancement>) =
        enhancements.map { "${it.name}$SEPARATOR_ENHANCEMENT${it.description}" }

    private fun parseLoadouts(allowedLoadouts: List<String>) = allowedLoadouts.map {
        Item.LoadoutType.valueOf(it)
    }

    private fun parseDamage(damage: String) =
        damage.let { Item.Damage.valueOf(it) }

    private fun parseWield(wield: String) =
        wield.let { Item.Weapon.Wield.valueOf(it) }

    private fun parseDamageTypes(damageTypes: List<String>) = damageTypes.map {
        Item.DamageType.valueOf(it)
    }.toSet()

    companion object {
        private const val SEPARATOR_ENHANCEMENT = ":"
    }
}