package com.pecawolf.cache

import com.pecawolf.cache.mapper.CharacterEntityMapper
import com.pecawolf.cache.mapper.ItemEntityMapper
import com.pecawolf.cache.model.ItemEntity
import com.pecawolf.common.exception.CharacterNotFoundException
import com.pecawolf.data.datasource.ICache
import com.pecawolf.data.model.CharacterData
import com.pecawolf.data.model.CharacterSnippetData
import com.pecawolf.data.model.ItemData
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import timber.log.Timber

class Cache(
    private val applicationPreferences: ApplicationPreferences,
    private val database: AppDatabase,
    private val characterMapper: CharacterEntityMapper,
    private val itemMapper: ItemEntityMapper,
) : ICache {

    override fun createCharacter(character: CharacterData): Completable = database.characterDao()
        .insert(characterMapper.toEntity(character))
        .doOnSuccess { applicationPreferences.activeCharacterId = it }
        .ignoreElement()

    override fun updateCharacter(character: CharacterData): Completable =
        database.characterDao().update(characterMapper.toEntity(character))

    override fun getCharacters(): Single<List<CharacterSnippetData>> = database.characterDao()
        .getAll()
        .map { list ->
            list.map {
                CharacterSnippetData(
                    it.characterId,
                    it.name,
                    it.species,
                    it.world
                )
            }
        }

    override fun setActiveCharacterId(characterId: Long?) {
        Timber.v("setActiveCharacterId(): $characterId")
        applicationPreferences.activeCharacterId = characterId
    }

    override fun getCharacter(characterId: Long?): Observable<CharacterData> =
        database.characterDao().getAllByIds(
            listOfNotNull(
                characterId ?: applicationPreferences.activeCharacterId
            ).toTypedArray()
        )
            .map {
                it.firstOrNull { it.characterId == characterId }
                    ?: throw CharacterNotFoundException(characterId)
            }
            .map { characterMapper.fromEntity(it) }

    override fun getItemsForOwner(ownerId: Long?): Observable<List<ItemData>> =
        database.itemDao()
            .getAllByOwnerId(
                ownerId ?: applicationPreferences.activeCharacterId ?: -1
            )
            .map { list -> list.map { item -> itemMapper.fromEntity(item) } }

    override fun createItemForCharacter(
        name: String,
        description: String,
        type: String,
        loadouts: List<String>,
        damage: String,
        wield: String,
        magazineSize: Int,
        rateOfFire: Int,
        damageTypes: List<String>,
        ownerId: Long?,
    ): Single<Long> = database.itemDao().insert(
        ItemEntity(
            0,
            ownerId
                ?: applicationPreferences.activeCharacterId
                ?: throw IllegalArgumentException("User id $ownerId not found!"),
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

    override fun getItemById(itemId: Long): Maybe<ItemData> = database.itemDao()
        .getById(itemId)
        .map { itemMapper.fromEntity(it) }
        .toMaybe()

    override fun updateItem(item: ItemData) = database.itemDao().update(itemMapper.toEntity(item))

    override fun deleteItem(itemId: Long) = database.itemDao().getById(itemId)
        .flatMapCompletable { database.itemDao().delete(it) }
}