package com.pecawolf.data.mapper

import com.pecawolf.cache.model.CharacterEntity
import com.pecawolf.charactersheet.common.extensions.isOneOf
import com.pecawolf.model.BaseStats
import com.pecawolf.model.Character
import com.pecawolf.model.Inventory
import com.pecawolf.model.Item
import com.pecawolf.model.Rollable
import com.pecawolf.model.Rollable.Stat.Charisma
import com.pecawolf.model.Rollable.Stat.Dexterity
import com.pecawolf.model.Rollable.Stat.Intelligence
import com.pecawolf.model.Rollable.Stat.Strength
import com.pecawolf.model.Rollable.Stat.Vitality
import com.pecawolf.model.Rollable.Stat.Wisdom
import com.pecawolf.model.Skills
import com.pecawolf.remote.model.StatSkillsResponse

class CharacterMapper {

    fun fromEntity(
        entity: CharacterEntity,
        items: List<Item>,
        skills: List<StatSkillsResponse>,
    ) = entity.run {

        val strength = Strength(str)
        val dexterity = Dexterity(dex)
        val vitality = Vitality(vit)
        val intelligence = Intelligence(inl)
        val wisdom = Wisdom(wis)
        val charisma = Charisma(cha)

        Character(
            characterId,
            BaseStats(
                name,
                BaseStats.Species.valueOf(species),
                BaseStats.World.valueOf(world),
                luck,
                wounds,
                strength,
                dexterity,
                vitality,
                intelligence,
                wisdom,
                charisma,
            ),
            Inventory(
                money,
                lookUpWeapon(items, primary),
                lookUpWeapon(items, secondary),
                lookUpWeapon(items, tertiary),
                lookUpArmor(items, clothes),
                lookUpArmor(items, armor),
                items,
            ),
            Skills(
                strength = lookupSkills(skills, strength, entity, STRENGTH, world),
                dexterity = lookupSkills(skills, dexterity, entity, DEXTERITY, world),
                vitality = lookupSkills(skills, vitality, entity, VITALITY, world),
                intelligence = lookupSkills(skills, intelligence, entity, INTELLIGENCE, world),
                wisdom = lookupSkills(skills, wisdom, entity, WISDOM, world),
                charisma = lookupSkills(skills, charisma, entity, CHARISMA, world),
            )
        )
    }

    fun toEntity(model: Character) = model.run {
        CharacterEntity(
            characterId,
            baseStats.name,
            baseStats.species.name,
            baseStats.world.name,
            baseStats.luck,
            baseStats.wounds,
            baseStats.str.value,
            baseStats.dex.value,
            baseStats.vit.value,
            baseStats.inl.value,
            baseStats.wis.value,
            baseStats.cha.value,
            inventory.money,
            inventory.primary.itemId,
            inventory.secondary.itemId,
            inventory.tertiary.itemId,
            inventory.clothes.itemId,
            inventory.armor.itemId,
            skills.run {
                listOf(
                    strength.map { it.code to it.value },
                    dexterity.map { it.code to it.value },
                    vitality.map { it.code to it.value },
                    intelligence.map { it.code to it.value },
                    wisdom.map { it.code to it.value },
                    charisma.map { it.code to it.value },
                )
                    .flatten()
                    .toMap()
                    .toMutableMap()
            }
        )
    }

    private fun lookUpWeapon(items: List<Item>, weaponId: Long) =
        (items.find { it.itemId == weaponId } as? Item.Weapon) ?: Item.Weapon.Melee.BareHands

    private fun lookUpArmor(items: List<Item>, armorId: Long) =
        (items.find { it.itemId == armorId } as? Item.Armor) ?: Item.Armor.None

    private fun lookupSkills(
        skills: List<StatSkillsResponse>,
        stat: Rollable.Stat,
        entity: CharacterEntity,
        statCode: String,
        world: String,
    ) = skills.firstOrNull { it.stat == statCode }
        ?.skills
        ?.filter {
            world.isOneOf(it.worlds)
        }
        ?.map { response ->
            Rollable.Skill(
                response.code,
                response.name["en"]!!, // TODO: Localize
                stat,
                entity.skills[response.code] ?: 0
            )
        }
        ?: listOf()

    companion object {
        private const val STRENGTH = "STR"
        private const val DEXTERITY = "DEX"
        private const val VITALITY = "VIT"
        private const val INTELLIGENCE = "INL"
        private const val WISDOM = "WIS"
        private const val CHARISMA = "CHA"
    }
}