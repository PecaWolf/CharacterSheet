package com.pecawolf.data.mapper

import com.pecawolf.data.model.CharacterData
import com.pecawolf.data.model.ItemData
import com.pecawolf.data.model.SkillsData
import com.pecawolf.model.BaseStats
import com.pecawolf.model.Character
import com.pecawolf.model.Inventory
import com.pecawolf.model.Item
import com.pecawolf.model.Rollable.Stat
import com.pecawolf.model.Skills

class CharacterFactory(
    private val itemMapper: ItemDataMapper,
    private val skillsMapper: SkillDataMapper,
) {

    fun createCharacter(
        character: CharacterData,
        itemsData: List<ItemData>,
        skillsData: List<SkillsData>,
    ): Character {
        val baseStats = character.run {
            BaseStats(
                name,
                BaseStats.Species.valueOf(species),
                BaseStats.World.valueOf(world),
                luck,
                wounds,
                Stat.Strength(str),
                Stat.Dexterity(dex),
                Stat.Vitality(vit),
                Stat.Intelligence(inl),
                Stat.Wisdom(wis),
                Stat.Charisma(cha),
            )
        }

        val items = itemsData.map { itemMapper.fromData(it) }

        val skills = skillsData.groupBy { it.stat }
            .map { Stat.Enum.valueOf(it.key) to it.value.flatMap { it.skills } }
            .toMap()


        val locale = "en" // TODO

        return Character(
            character.characterId,
            baseStats,
            Inventory(
                character.money,
                items.firstOrNull { it.itemId == character.primary } as? Item.Weapon
                    ?: Item.Weapon.Melee.BareHands,
                items.firstOrNull { it.itemId == character.secondary } as? Item.Weapon
                    ?: Item.Weapon.Melee.BareHands,
                items.firstOrNull { it.itemId == character.tertiary } as? Item.Weapon
                    ?: Item.Weapon.Melee.BareHands,
                items.firstOrNull { it.itemId == character.clothes } as? Item.Armor
                    ?: Item.Armor.None,
                items.firstOrNull { it.itemId == character.armor } as? Item.Armor
                    ?: Item.Armor.None,
            ),
            Skills(
                skills[Stat.Enum.STR]?.map {
                    skillsMapper.fromData(it, baseStats.str, locale)
                } ?: listOf(),
                skills[Stat.Enum.DEX]?.map {
                    skillsMapper.fromData(it, baseStats.dex, locale)
                } ?: listOf(),
                skills[Stat.Enum.VIT]?.map {
                    skillsMapper.fromData(it, baseStats.vit, locale)
                } ?: listOf(),
                skills[Stat.Enum.INL]?.map {
                    skillsMapper.fromData(it, baseStats.inl, locale)
                } ?: listOf(),
                skills[Stat.Enum.WIS]?.map {
                    skillsMapper.fromData(it, baseStats.wis, locale)
                } ?: listOf(),
                skills[Stat.Enum.CHA]?.map {
                    skillsMapper.fromData(it, baseStats.cha, locale)
                } ?: listOf(),
            )
        )
    }
}