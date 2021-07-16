package com.pecawolf.cache

import com.pecawolf.cache.model.CharacterEntity
import com.pecawolf.cache.model.CharacterSnippetEntity
import com.pecawolf.cache.model.ItemEntity
import io.reactivex.rxjava3.core.Maybe
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

    fun setActiveCharacterId(characterId: Long?) {
        applicationPreferences.activeCharacterId = characterId
    }

    fun getCharacter(characterId: Long? = applicationPreferences.activeCharacterId) =
        if (characterId == null) Maybe.empty()
        else database.characterDao().getAllByIds(arrayOf(characterId))
            .flatMapMaybe { chars -> chars.firstOrNull()?.let { Maybe.just(it) } ?: Maybe.empty() }

    fun getItemsForOwner(ownerId: Long) = database.itemDao()
        .getAllByOwnerId(ownerId)

    fun createItemForCharacter(
        name: String,
        description: String,
        type: String,
        loadouts: List<String>,
        damage: String,
        wield: String,
        magazineSize: Int,
        rateOfFire: Int,
        damageTypes: List<String>,
        ownerId: Long? = applicationPreferences.activeCharacterId,
    ): Single<Long> = database.itemDao().insert(
        ItemEntity(
            0,
            ownerId ?: throw IllegalArgumentException("User id $ownerId not found!"),
            type,
            name,
            description,
            1,
            loadouts,
            listOf(),
            damage,
            wield,
            damageTypes,
            magazineSize,
            rateOfFire
        )
    )

    fun getItemById(itemId: Long): Maybe<ItemEntity> = database.itemDao()
        .getAllByIds(arrayOf(itemId))
        .flatMapMaybe { items -> items.firstOrNull()?.let { Maybe.just(it) } ?: Maybe.empty() }
}