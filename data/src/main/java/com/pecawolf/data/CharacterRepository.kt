package com.pecawolf.data

import com.pecawolf.cache.Cache
import com.pecawolf.cache.model.CharacterEntity
import com.pecawolf.data.mapper.CharacterMapper
import com.pecawolf.data.mapper.CharacterSnippetMapper
import com.pecawolf.data.mapper.ItemMapper
import com.pecawolf.model.BaseStats
import com.pecawolf.model.Character
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class CharacterRepository(
    private val cache: Cache,
    private val characterMapper: CharacterMapper,
    private val chracterSnippetMapper: CharacterSnippetMapper,
    val itemMapper: ItemMapper
) {
    private val activeCharacter: BehaviorSubject<List<CharacterEntity>> = BehaviorSubject.create()

    fun createCharacter(baseStats: BaseStats) =
        cache.createCharacter(characterMapper.toEntity(Character.new(baseStats)))

    fun getActiveCharacter() = cache.getCharacter()
        .doOnSuccess { activeCharacter.onNext(listOf(it)) }
        .flatMap { character ->
            cache.getItemsForOwner(character.characterId)
                .map { list -> list.map { itemMapper.fromEntity(it) } }
                .map { items -> characterMapper.fromEntity(character, items) }
                .toMaybe()
        }

    fun observeActiveCharacter() = activeCharacter
        .map { list -> list.map { characterMapper.fromEntity(it) } }

    fun getCharacterSnippets() = cache.getCharacters()
        .map { characters -> characters.map { chracterSnippetMapper.fromEntity(it) } }

    fun setActiveCharacterId(characterId: Long) = cache.getCharacter(characterId)
        .doOnSuccess { cache.setActiveCharacterId(characterId) }
        .doOnSuccess { activeCharacter.onNext(listOf(it)) }
        .ignoreElement()

    fun clearActiveCharacter(): Completable {
        return Completable.complete()
            .doOnComplete { activeCharacter.onNext(listOf()) }
            .doOnComplete { cache.setActiveCharacterId(null) }
    }
}