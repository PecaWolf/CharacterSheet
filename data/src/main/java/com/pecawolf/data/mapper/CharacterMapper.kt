package com.pecawolf.data.mapper

import com.pecawolf.cache.model.CharacterEntity
import com.pecawolf.model.BaseStats
import com.pecawolf.model.Character
import com.pecawolf.model.Inventory
import com.pecawolf.model.Item

class CharacterMapper : BaseMapper<Character, CharacterEntity, Nothing, Item> {

    override fun fromEntity(entity: CharacterEntity, items: List<Item>) = entity.run {
        Character(
            characterId,
            BaseStats(
                name,
                BaseStats.Species.valueOf(species),
                BaseStats.World.valueOf(world),
                luck,
                wounds,
                str,
                dex,
                vit,
                inl,
                wis,
                cha
            ),
            Inventory(
                money,
                lookUpWeapon(items, primary),
                lookUpWeapon(items, secondary),
                lookUpWeapon(items, tertiary),
                lookUpArmor(items, clothes),
                lookUpArmor(items, armor),
                filterContainers(items),
            )
        )
    }

    private fun lookUpWeapon(items: List<Item>, weaponId: Long) =
        (items.find { it.itemId == weaponId } as? Item.Weapon) ?: Item.Weapon.Melee.BareHands

    private fun lookUpArmor(items: List<Item>, armorId: Long) =
        (items.find { it.itemId == armorId } as? Item.Armor) ?: Item.Armor.None

    private fun CharacterEntity.filterContainers(items: List<Item>) =
        items
//            .filter { it.itemId.isNotOneOf(primary, secondary, tertiary, armor, clothes) }

    override fun toEntity(model: Character, additional: List<Nothing>) = model.run {
        CharacterEntity(
            characterId,
            baseStats.name,
            baseStats.species.name,
            baseStats.world.name,
            baseStats.luck,
            baseStats.wounds,
            baseStats.str,
            baseStats.dex,
            baseStats.vit,
            baseStats.inl,
            baseStats.wis,
            baseStats.cha,
            inventory.money,
            inventory.primary.itemId,
            inventory.secondary.itemId,
            inventory.tertiary.itemId,
            inventory.clothes.itemId,
            inventory.armor.itemId,
            inventory.backpack.map { it.itemId }.toMutableList(),
        )
    }
}