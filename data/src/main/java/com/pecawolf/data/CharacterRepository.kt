package com.pecawolf.data

import com.pecawolf.cache.Cache
import com.pecawolf.cache.model.CharacterEntity
import com.pecawolf.data.mapper.CharacterMapper
import com.pecawolf.data.mapper.CharacterSnippetMapper
import com.pecawolf.data.mapper.ItemMapper
import com.pecawolf.model.BaseStats
import com.pecawolf.model.Character
import com.pecawolf.model.Item
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import timber.log.Timber

class CharacterRepository(
    private val cache: Cache,
    private val characterMapper: CharacterMapper,
    private val chracterSnippetMapper: CharacterSnippetMapper,
    private val itemMapper: ItemMapper
) {
    private val activeCharacter: BehaviorSubject<List<CharacterEntity>> = BehaviorSubject.create()

    fun createCharacter(baseStats: BaseStats) =
        cache.createCharacter(characterMapper.toEntity(Character.new(baseStats)))

    fun getActiveCharacter() = cache.getCharacter()
        .doOnSuccess { activeCharacter.onNext(listOf(it)) }
        .flatMap { getItemsForCharacter(it).toMaybe() }

    fun observeActiveCharacter(): Observable<List<Character>> = activeCharacter
        .flatMap { list ->
            list.firstOrNull()?.let { character ->
                getItemsForCharacter(character)
                    .map { listOf(it) }
                    .toObservable()
            } ?: Observable.just(listOf())
        }

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

    fun createItemForActiveCharacter(
        name: String,
        description: String,
        type: Item.ItemType,
        loadouts: List<Item.LoadoutType>,
        damage: Item.Damage,
        wield: Item.Weapon.Wield?,
        magazineSize: Int,
        rateOfFire: Int,
        damageTypes: MutableSet<Item.DamageType>
    ): Single<Long> = cache.createItemForCharacter(
        name,
        description,
        type.name,
        loadouts.map { it.name },
        damage.name,
        wield?.name ?: "",
        magazineSize,
        rateOfFire,
        damageTypes.map { it.name }
    ).doOnSuccess { activeCharacter.onNext(activeCharacter.value) }

    fun getItem(itemId: Long): Maybe<Item> = cache.getItemById(itemId)
        .map { itemMapper.fromEntity(it) }

    private fun getItemsForCharacter(character: CharacterEntity) =
        cache.getItemsForOwner(character.characterId)
            .doOnSuccess { Timber.d("getItemsForCharacter(): ${character.characterId}, ${it}") }
            .map { list -> list.map { itemMapper.fromEntity(it) } }
            .map { items -> characterMapper.fromEntity(character, items) }
}