package com.pecawolf.data.mapper

import com.pecawolf.cache.model.CharacterSnippetEntity
import com.pecawolf.model.BaseStats
import com.pecawolf.model.CharacterSnippet

class CharacterSnippetMapper {

    fun fromEntity(entity: CharacterSnippetEntity) = entity.run {
        CharacterSnippet(
            characterId,
            name,
            BaseStats.Species.valueOf(species),
            BaseStats.World.valueOf(world)
        )
    }
}
