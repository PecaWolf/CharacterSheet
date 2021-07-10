package com.pecawolf.data

import com.pecawolf.cache.Cache
import com.pecawolf.cache.model.CharacterEntity
import com.pecawolf.data.mapper.CharacterMapper
import com.pecawolf.data.mapper.CharacterSnippetMapper
import com.pecawolf.model.BaseStats
import com.pecawolf.model.Character
import io.reactivex.rxjava3.subjects.BehaviorSubject

class CharacterRepository(
    private val characterCache: Cache,
    private val characterMapper: CharacterMapper,
    private val chracterSnippetMapper: CharacterSnippetMapper
) {
    private val activeCharacter: BehaviorSubject<CharacterEntity> = BehaviorSubject.create()

    fun createCharacter(baseStats: BaseStats) =
        characterCache.createCharacter(characterMapper.toEntity(Character.new(baseStats)))

    fun getActiveCharacter() = characterCache.getCharacter()
        .doOnSuccess { activeCharacter.onNext(it) }
        .map { characterMapper.fromEntity(it) }

    fun observeActiveCharacter() = activeCharacter
        .map { characterMapper.fromEntity(it) }

    fun getCharacterSnippets() = characterCache.getCharacters()
        .map { characters -> characters.map { chracterSnippetMapper.fromEntity(it) } }

    fun setActiveCharacterId(characterId: Long) = characterCache.getCharacter(characterId)
        .doOnSuccess { characterCache.setActiveCharacterId(characterId) }
        .doOnSuccess { activeCharacter.onNext(it) }
        .ignoreElement()
}