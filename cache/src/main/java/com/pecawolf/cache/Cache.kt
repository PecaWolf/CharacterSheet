package com.pecawolf.cache

import android.content.Context
import com.pecawolf.cache.mapper.CharacterEntityMapper
import com.pecawolf.cache.mapper.ItemEntityMapper
import com.pecawolf.cache.mapper.SkillEntityMapper
import com.pecawolf.cache.model.CharacterEntity
import com.pecawolf.cache.model.ItemEntity
import com.pecawolf.cache.model.SkillsEntity
import com.pecawolf.common.exception.CharacterNotFoundException
import com.pecawolf.data.datasource.ICache
import com.pecawolf.data.model.CharacterData
import com.pecawolf.data.model.CharacterSnippetData
import com.pecawolf.data.model.ItemData
import com.pecawolf.data.model.SkillsData
import com.squareup.moshi.Moshi
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.io.IOException
import java.nio.charset.Charset
import timber.log.Timber

class Cache(
    private val context: Context,
    private val moshi: Moshi,
    private val applicationPreferences: ApplicationPreferences,
    private val database: AppDatabase,
    private val characterMapper: CharacterEntityMapper,
    private val itemMapper: ItemEntityMapper,
    private val skillMapper: SkillEntityMapper,
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
        (characterId ?: applicationPreferences.activeCharacterId)?.let {
            observeCharacter(it)
        } ?: Observable.error(IllegalStateException("Character Id cannot be null"))

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
        magazineCount: Int,
        magazineState: Int,
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
            rateOfFire,
            magazineCount,
            magazineState,
        )
    )

    override fun getItemById(itemId: Long): Maybe<ItemData> = database.itemDao()
        .getById(itemId)
        .map { itemMapper.fromEntity(it) }
        .toMaybe()

    override fun updateItem(item: ItemData) = database.itemDao().update(itemMapper.toEntity(item))

    override fun deleteItem(itemId: Long) = database.itemDao().getById(itemId)
        .flatMapCompletable { database.itemDao().delete(it) }

    override fun getSkills(): Single<List<SkillsData>> = Single.just(loadJSONFromAsset())
        .map { moshi.adapter(SkillsEntity::class.java).fromJson(it)?.availableSkills ?: listOf() }
        .map { list -> list.map { skillMapper.fromEntity(it) } }

    private fun observeCharacter(characterId: Long) = database.characterDao()
        .getAllByIds(listOf(characterId).toTypedArray())
        .map { findCharacter(it, characterId) }
        .map { characterMapper.fromEntity(it) }

    private fun findCharacter(characters: List<CharacterEntity>, characterId: Long) =
        characters.firstOrNull { it.characterId == characterId }
            ?: throw CharacterNotFoundException(characterId)

    private fun loadJSONFromAsset() = try {
        context.assets?.open("skills.json")?.let { stream ->
            val size: Int = stream.available()
            val buffer = ByteArray(size)
            stream.read(buffer)
            stream.close()
            String(buffer, Charset.forName("UTF-8"))
        } ?: ""
    } catch (ex: IOException) {
        ex.printStackTrace()
        ""
    }
}