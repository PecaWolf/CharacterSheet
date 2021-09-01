package com.pecawolf.cache.mapper

import com.pecawolf.cache.model.CharacterEntity
import com.pecawolf.data.model.CharacterData

class CharacterEntityMapper {

    fun fromEntity(
        entity: CharacterEntity,
    ) = entity.run {
        CharacterData(
            characterId,
            name,
            species,
            world,
            luck,
            wounds,
            str,
            dex,
            vit,
            inl,
            wis,
            cha,
            money,
            primary,
            secondary,
            tertiary,
            clothes,
            armor,
            skills,
        )
    }

    fun toEntity(model: CharacterData) = model.run {
        CharacterEntity(
            characterId,
            name,
            species,
            world,
            luck,
            wounds,
            str,
            dex,
            vit,
            inl,
            wis,
            cha,
            money,
            primary,
            secondary,
            tertiary,
            clothes,
            armor,
            skills,
        )
    }

    companion object {
        private const val STRENGTH = "STR"
        private const val DEXTERITY = "DEX"
        private const val VITALITY = "VIT"
        private const val INTELLIGENCE = "INL"
        private const val WISDOM = "WIS"
        private const val CHARISMA = "CHA"
    }
}