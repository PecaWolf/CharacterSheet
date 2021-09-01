package com.pecawolf.data.mapper

import com.pecawolf.data.model.CharacterSnippetData
import com.pecawolf.model.BaseStats
import com.pecawolf.model.CharacterSnippet

class CharacterSnippetDataMapper {
    fun fromData(data: CharacterSnippetData) = data.run {
        CharacterSnippet(
            characterId,
            name,
            BaseStats.Species.valueOf(species),
            BaseStats.World.valueOf(world)
        )
    }
}