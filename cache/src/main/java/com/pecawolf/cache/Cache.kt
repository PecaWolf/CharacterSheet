package com.pecawolf.cache

import com.pecawolf.cache.model.CharacterEntity
import com.pecawolf.cache.model.CharacterSnippetEntity
import com.pecawolf.cache.model.ItemEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

class Cache(
    private val applicationPreferences: ApplicationPreferences,
    private val database: AppDatabase
) {

    fun createCharacter(character: CharacterEntity) = database.characterDao().insert(character)
        .doOnSuccess { applicationPreferences.activeCharacterId = it }
        .ignoreElement()

    fun updateCharacter(character: CharacterEntity): Completable =
        database.characterDao().update(character)

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
        database.characterDao().getAllByIds(listOfNotNull(characterId).toTypedArray())
            .map { it.first { it.characterId == characterId } }

    fun getItemsForOwner(ownerId: Long? = applicationPreferences.activeCharacterId) =
        database.itemDao().getAllByOwnerId(ownerId ?: -1)

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

    fun updateItem(item: ItemEntity) = database.itemDao().update(item)
}