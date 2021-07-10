package com.pecawolf.cache

import com.pecawolf.cache.model.CharacterEntity
import com.pecawolf.cache.model.CharacterSnippetEntity
import com.pecawolf.charactersheet.common.exception.CharacterNotFoundException
import io.reactivex.rxjava3.core.Single

class Cache(
    private val applicationPreferences: ApplicationPreferences,
    private val database: AppDatabase
) {

    fun createCharacter(character: CharacterEntity) = database.characterDao().insert(character)
        .doOnSuccess { applicationPreferences.activeCharacterId = it }
        .ignoreElement()

    fun getCharacters(): Single<List<CharacterSnippetEntity>> {
        return database.characterDao()
            .getAll()
            .map { list ->
                list.map {
                    CharacterSnippetEntity(
                        it.characterId,
                        it.name,
                        it.species,
                        it.world
                    )
                }
            }
    }

    fun setActiveCharacterId(characterId: Long) {
        applicationPreferences.activeCharacterId = characterId
    }

    fun getCharacter(characterId: Long? = applicationPreferences.activeCharacterId) =
        if (characterId == null) Single.error(CharacterNotFoundException(-1))
        else database.characterDao().loadAllByIds(arrayOf(characterId))
            .flatMap {
                it.firstOrNull()?.let { Single.just(it) }
                    ?: Single.error(CharacterNotFoundException(characterId))
            }
}