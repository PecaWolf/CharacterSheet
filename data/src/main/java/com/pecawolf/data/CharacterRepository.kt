package com.pecawolf.data

import com.pecawolf.cache.Cache
import com.pecawolf.data.mapper.CharacterMapper
import com.pecawolf.model.Character
import io.reactivex.rxjava3.core.Single

class CharacterRepository(
    private val characterCache: Cache,
    private val characterMapper: CharacterMapper
) {

    fun getActiveCharacter(): Single<Character> =
        characterCache.getActiveCharacter()
            .map { characterMapper.fromEntity(it) }
}