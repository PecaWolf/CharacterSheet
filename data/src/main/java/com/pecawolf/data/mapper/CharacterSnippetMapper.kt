package com.pecawolf.data.mapper

import com.pecawolf.cache.model.CharacterSnippetEntity
import com.pecawolf.model.BaseStats
import com.pecawolf.model.CharacterSnippet

class CharacterSnippetMapper :
    BaseMapper<CharacterSnippet, CharacterSnippetEntity, Nothing, Nothing> {

    override fun fromEntity(
        entity: CharacterSnippetEntity,
        additional: List<Nothing>
    ) = entity.run {
        CharacterSnippet(
            characterId,
            name,
            BaseStats.Species.valueOf(species),
            BaseStats.World.valueOf(world)
        )
    }

    override fun toEntity(
        model: CharacterSnippet,
        additional: List<Nothing>
    ) = model.run { CharacterSnippetEntity(characterId, name, species.name, world.name) }
}
