package com.pecawolf.cache

import com.pecawolf.cache.model.CharacterEntity
import com.pecawolf.cache.model.CharacterSnippetEntity
import com.pecawolf.charactersheet.common.exception.CharacterNotFoundException
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

class Cache(private val applicationPreferences: ApplicationPreferences) {

    fun getCharacters(): Single<List<CharacterSnippetEntity>> {
        return Single.just(listOf())
    }

    fun getCharacter(characterId: Long? = applicationPreferences.activeCharacterId): Maybe<CharacterEntity> {
        applicationPreferences.activeCharacterId == null
        return Maybe.error(CharacterNotFoundException(123))
    }
}