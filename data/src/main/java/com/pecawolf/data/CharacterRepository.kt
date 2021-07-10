package com.pecawolf.data

import com.pecawolf.cache.Cache
import com.pecawolf.data.mapper.CharacterMapper
import com.pecawolf.data.mapper.CharacterSnippetMapper
import com.pecawolf.model.BaseStats
import com.pecawolf.model.Character
import io.reactivex.rxjava3.core.Completable

class CharacterRepository(
    private val characterCache: Cache,
    private val characterMapper: CharacterMapper,
    private val chracterSnippetMapper: CharacterSnippetMapper
) {
    fun createCharacter(baseStats: BaseStats) =
        characterCache.createCharacter(characterMapper.toEntity(Character.new(baseStats)))

    fun getActiveCharacter() = characterCache.getCharacter()
        .map { characterMapper.fromEntity(it) }

    fun getCharacterSnippets() = characterCache.getCharacters()
        .map { characters -> characters.map { chracterSnippetMapper.fromEntity(it) } }

    fun setActiveCharacter(characterId: Long) = Completable.complete()
}