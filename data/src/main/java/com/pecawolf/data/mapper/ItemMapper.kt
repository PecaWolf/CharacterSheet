package com.pecawolf.data.mapper

import com.pecawolf.cache.model.ItemEntity
import com.pecawolf.model.Item
import com.pecawolf.model.Item.Weapon.Melee.BareHands.wield

class ItemMapper : BaseMapper<Item, ItemEntity, Long, Nothing> {

    override fun fromEntity(entity: ItemEntity, additional: List<Nothing>) = entity.run {
        when (Item.ItemType.valueOf(type)) {
            Item.ItemType.BARE_HANDS -> Item.Weapon.Melee.BareHands
            Item.ItemType.KNIFE -> Item.Weapon.Melee.Knife(
                itemId,
                name,
                description,
                count,
                parseEnhancements(enhancements),
                parseLoadouts(allowedLoadouts),
                parseDamage(damage),
                parseDamageTypes(damageTypes)
            )
            Item.ItemType.SWORD -> Item.Weapon.Melee.Sword(
                itemId,
                name,
                description,
                count,
                parseEnhancements(enhancements),
                parseLoadouts(allowedLoadouts),
                parseDamage(damage),
                parseWield(wield),
                parseDamageTypes(damageTypes)
            )
            Item.ItemType.AXE -> Item.Weapon.Melee.Axe(
                itemId,
                name,
                description,
                count,
                parseEnhancements(enhancements),
                parseLoadouts(allowedLoadouts),
                parseDamage(damage),
                parseWield(wield),
                parseDamageTypes(damageTypes)
            )
            Item.ItemType.BOW -> Item.Weapon.Ranged.Bow(
                itemId,
                name,
                description,
                count,
                parseEnhancements(enhancements),
                parseLoadouts(allowedLoadouts),
                parseDamage(damage),
                parseDamageTypes(damageTypes),
                magazine,
                rateOfFire
            )
            Item.ItemType.CROSSBOW -> Item.Weapon.Ranged.Crossbow(
                itemId,
                name,
                description,
                count,
                parseEnhancements(enhancements),
                parseLoadouts(allowedLoadouts),
                parseDamage(damage),
                parseDamageTypes(damageTypes),
                magazine,
                rateOfFire
            )
            Item.ItemType.PISTOL -> Item.Weapon.Ranged.Pistol(
                itemId,
                name,
                description,
                count,
                parseEnhancements(enhancements),
                parseLoadouts(allowedLoadouts),
                parseDamage(damage),
                parseDamageTypes(damageTypes),
                magazine,
                rateOfFire
            )
            Item.ItemType.REVOLVER -> Item.Weapon.Ranged.Revolver(
                itemId,
                name,
                description,
                count,
                parseEnhancements(enhancements),
                parseLoadouts(allowedLoadouts),
                parseDamage(damage),
                parseDamageTypes(damageTypes),
                magazine,
                rateOfFire
            )
            Item.ItemType.RIFLE -> Item.Weapon.Ranged.Rifle(
                itemId,
                name,
                description,
                count,
                parseEnhancements(enhancements),
                parseLoadouts(allowedLoadouts),
                parseDamage(damage),
                parseDamageTypes(damageTypes),
                magazine,
                rateOfFire
            )
            Item.ItemType.SUBMACHINE_GUN -> Item.Weapon.Ranged.SubmachineGun(
                itemId,
                name,
                description,
                count,
                parseEnhancements(enhancements),
                parseLoadouts(allowedLoadouts),
                parseDamage(damage),
                parseDamageTypes(damageTypes),
                magazine,
                rateOfFire
            )
            Item.ItemType.SHOTGUN -> Item.Weapon.Ranged.Shotgun(
                itemId,
                name,
                description,
                count,
                parseEnhancements(enhancements),
                parseLoadouts(allowedLoadouts),
                parseDamage(damage),
                parseDamageTypes(damageTypes),
                magazine,
                rateOfFire
            )
            Item.ItemType.MACHINE_GUN -> Item.Weapon.Ranged.MachineGun(
                itemId,
                name,
                description,
                count,
                parseEnhancements(enhancements),
                parseLoadouts(allowedLoadouts),
                parseDamage(damage),
                parseDamageTypes(damageTypes),
                magazine,
                rateOfFire
            )
            Item.ItemType.ANTIMATERIAL_GUN -> Item.Weapon.Ranged.AntimaterialGun(
                itemId,
                name,
                description,
                count,
                parseEnhancements(enhancements),
                parseLoadouts(allowedLoadouts),
                parseDamage(damage),
                parseDamageTypes(damageTypes),
                magazine,
                rateOfFire
            )
            Item.ItemType.GRENADE -> Item.Weapon.Grenade(
                itemId,
                name,
                description,
                count,
                parseEnhancements(enhancements),
                parseDamage(damage),
                parseDamageTypes(damageTypes)
            )
            Item.ItemType.NONE -> Item.Armor.None
            Item.ItemType.CLOTHING -> Item.Armor.Clothing(
                itemId,
                name,
                description,
                count,
                parseEnhancements(enhancements),
                parseDamageTypes(damageTypes)
            )
            Item.ItemType.KEVLAR -> Item.Armor.Kevlar(
                itemId,
                name,
                description,
                count,
                parseEnhancements(enhancements),
                parseLoadouts(allowedLoadouts),
                parseDamage(damage),
                parseDamageTypes(damageTypes)
            )
            Item.ItemType.EXO_SKELETON -> Item.Armor.ExoSkeleton(
                itemId,
                name,
                description,
                count,
                parseEnhancements(enhancements),
                parseLoadouts(allowedLoadouts),
                parseDamage(damage),
                parseDamageTypes(damageTypes)
            )
            Item.ItemType.VAC_SUIT -> Item.Armor.VacSuit(
                itemId,
                name,
                description,
                count,
                parseEnhancements(enhancements),
                parseLoadouts(allowedLoadouts),
                parseDamage(damage),
                parseDamageTypes(damageTypes)
            )
            Item.ItemType.VAC_ARMOR -> Item.Armor.VacArmor(
                itemId,
                name,
                description,
                count,
                parseEnhancements(enhancements),
                parseLoadouts(allowedLoadouts),
                parseDamage(damage),
                parseDamageTypes(damageTypes)
            )
            Item.ItemType.POWERED_ARMOR -> Item.Armor.PoweredArmor(
                itemId,
                name,
                description,
                count,
                parseEnhancements(enhancements),
                parseLoadouts(allowedLoadouts),
                parseDamage(damage),
                parseDamageTypes(damageTypes)
            )
            Item.ItemType.OTHER -> Item.Other(
                itemId,
                name,
                description,
                count,
                parseEnhancements(enhancements)
            )
        }
    }

    override fun toEntity(model: Item, additional: List<Long>) = model.run {
        val ownerId = additional[0]

        // TODO: Simplify
        when (this) {
            is Item.Weapon.Melee.BareHands -> ItemEntity(
                itemId,
                ownerId,
                Item.ItemType.BARE_HANDS.name,
                name,
                description,
                count,
                allowedLoadouts.map { it.name },
                codeEnhancements(enhancements),
                damage.name,
                wield.name,
                damageTypes.map { it.name },
                -1,
                -1,
            )
            is Item.Weapon.Melee.Knife -> ItemEntity(
                itemId,
                ownerId,
                Item.ItemType.KNIFE.name,
                name,
                description,
                count,
                allowedLoadouts.map { it.name },
                codeEnhancements(enhancements),
                damage.name,
                wield.name,
                damageTypes.map { it.name },
                -1,
                -1,
            )
            is Item.Weapon.Melee.Sword -> ItemEntity(
                itemId,
                ownerId,
                Item.ItemType.SWORD.name,
                name,
                description,
                count,
                allowedLoadouts.map { it.name },
                codeEnhancements(enhancements),
                damage.name,
                wield.name,
                damageTypes.map { it.name },
                -1,
                -1,
            )
            is Item.Weapon.Melee.Axe -> ItemEntity(
                itemId,
                ownerId,
                Item.ItemType.AXE.name,
                name,
                description,
                count,
                allowedLoadouts.map { it.name },
                codeEnhancements(enhancements),
                damage.name,
                wield.name,
                damageTypes.map { it.name },
                -1,
                -1,
            )
            is Item.Weapon.Ranged.Bow -> ItemEntity(
                itemId,
                ownerId,
                Item.ItemType.BOW.name,
                name,
                description,
                count,
                allowedLoadouts.map { it.name },
                codeEnhancements(enhancements),
                damage.name,
                wield.name,
                damageTypes.map { it.name },
                magazine,
                rateOfFire,
            )
            is Item.Weapon.Ranged.Crossbow -> ItemEntity(
                itemId,
                ownerId,
                Item.ItemType.CROSSBOW.name,
                name,
                description,
                count,
                allowedLoadouts.map { it.name },
                codeEnhancements(enhancements),
                damage.name,
                wield.name,
                damageTypes.map { it.name },
                magazine,
                rateOfFire,
            )
            is Item.Weapon.Ranged.Pistol -> ItemEntity(
                itemId,
                ownerId,
                Item.ItemType.PISTOL.name,
                name,
                description,
                count,
                allowedLoadouts.map { it.name },
                codeEnhancements(enhancements),
                damage.name,
                wield.name,
                damageTypes.map { it.name },
                magazine,
                rateOfFire,
            )
            is Item.Weapon.Ranged.Revolver -> ItemEntity(
                itemId,
                ownerId,
                Item.ItemType.REVOLVER.name,
                name,
                description,
                count,
                allowedLoadouts.map { it.name },
                codeEnhancements(enhancements),
                damage.name,
                wield.name,
                damageTypes.map { it.name },
                magazine,
                rateOfFire,
            )
            is Item.Weapon.Ranged.Rifle -> ItemEntity(
                itemId,
                ownerId,
                Item.ItemType.RIFLE.name,
                name,
                description,
                count,
                allowedLoadouts.map { it.name },
                codeEnhancements(enhancements),
                damage.name,
                wield.name,
                damageTypes.map { it.name },
                magazine,
                rateOfFire,
            )
            is Item.Weapon.Ranged.SubmachineGun -> ItemEntity(
                itemId,
                ownerId,
                Item.ItemType.SUBMACHINE_GUN.name,
                name,
                description,
                count,
                allowedLoadouts.map { it.name },
                codeEnhancements(enhancements),
                damage.name,
                wield.name,
                damageTypes.map { it.name },
                magazine,
                rateOfFire,
            )
            is Item.Weapon.Ranged.Shotgun -> ItemEntity(
                itemId,
                ownerId,
                Item.ItemType.SHOTGUN.name,
                name,
                description,
                count,
                allowedLoadouts.map { it.name },
                codeEnhancements(enhancements),
                damage.name,
                wield.name,
                damageTypes.map { it.name },
                magazine,
                rateOfFire,
            )
            is Item.Weapon.Ranged.MachineGun -> ItemEntity(
                itemId,
                ownerId,
                Item.ItemType.MACHINE_GUN.name,
                name,
                description,
                count,
                allowedLoadouts.map { it.name },
                codeEnhancements(enhancements),
                damage.name,
                wield.name,
                damageTypes.map { it.name },
                magazine,
                rateOfFire,
            )
            is Item.Weapon.Ranged.AntimaterialGun -> ItemEntity(
                itemId,
                ownerId,
                Item.ItemType.ANTIMATERIAL_GUN.name,
                name,
                description,
                count,
                allowedLoadouts.map { it.name },
                codeEnhancements(enhancements),
                damage.name,
                wield.name,
                damageTypes.map { it.name },
                magazine,
                rateOfFire,
            )
            is Item.Weapon.Grenade -> ItemEntity(
                itemId,
                ownerId,
                Item.ItemType.ANTIMATERIAL_GUN.name,
                name,
                description,
                count,
                allowedLoadouts.map { it.name },
                codeEnhancements(enhancements),
                damage.name,
                wield.name,
                damageTypes.map { it.name },
                -1,
                -1,
            )
            is Item.Armor.None -> ItemEntity(
                itemId,
                ownerId,
                Item.ItemType.NONE.name,
                name,
                description,
                count,
                allowedLoadouts.map { it.name },
                listOf(),
                Item.Damage.NONE.name,
                "",
                damageTypes.map { it.name },
                -1,
                -1,
            )
            is Item.Armor.Clothing -> ItemEntity(
                itemId,
                ownerId,
                Item.ItemType.CLOTHING.name,
                name,
                description,
                count,
                allowedLoadouts.map { it.name },
                codeEnhancements(enhancements),
                Item.Damage.NONE.name,
                "",
                damageTypes.map { it.name },
                -1,
                -1,
            )
            is Item.Armor.Kevlar -> ItemEntity(
                itemId,
                ownerId,
                Item.ItemType.KEVLAR.name,
                name,
                description,
                count,
                allowedLoadouts.map { it.name },
                codeEnhancements(enhancements),
                damage.name,
                "",
                damageTypes.map { it.name },
                -1,
                -1,
            )
            is Item.Armor.ExoSkeleton -> ItemEntity(
                itemId,
                ownerId,
                Item.ItemType.EXO_SKELETON.name,
                name,
                description,
                count,
                allowedLoadouts.map { it.name },
                codeEnhancements(enhancements),
                damage.name,
                "",
                damageTypes.map { it.name },
                -1,
                -1,
            )
            is Item.Armor.VacSuit -> ItemEntity(
                itemId,
                ownerId,
                Item.ItemType.VAC_SUIT.name,
                name,
                description,
                count,
                allowedLoadouts.map { it.name },
                codeEnhancements(enhancements),
                damage.name,
                "",
                damageTypes.map { it.name },
                -1,
                -1,
            )
            is Item.Armor.VacArmor -> ItemEntity(
                itemId,
                ownerId,
                Item.ItemType.VAC_ARMOR.name,
                name,
                description,
                count,
                allowedLoadouts.map { it.name },
                codeEnhancements(enhancements),
                damage.name,
                "",
                damageTypes.map { it.name },
                -1,
                -1,
            )
            is Item.Armor.PoweredArmor -> ItemEntity(
                itemId,
                ownerId,
                Item.ItemType.POWERED_ARMOR.name,
                name,
                description,
                count,
                allowedLoadouts.map { it.name },
                codeEnhancements(enhancements),
                damage.name,
                "",
                damageTypes.map { it.name },
                -1,
                -1,
            )
            is Item.Other -> ItemEntity(
                itemId,
                ownerId,
                Item.ItemType.OTHER.name,
                name,
                description,
                count,
                allowedLoadouts.map { it.name },
                codeEnhancements(enhancements),
                "",
                "",
                listOf(),
                -1,
                -1,
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
    }.toMutableList()

    private fun parseDamage(damage: String) =
        damage.let { Item.Damage.valueOf(it) }

    private fun parseWield(wield: String) =
        wield.let { Item.Weapon.Wield.valueOf(it) }

    private fun parseDamageTypes(damageTypes: List<String>) = damageTypes.map {
        Item.DamageType.valueOf(it)
    }.toMutableSet()

    companion object {
        private const val SEPARATOR_ENHANCEMENT = ":"
    }
}