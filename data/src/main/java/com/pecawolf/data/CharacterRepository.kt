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
import timber.log.Timber

class CharacterRepository(
    private val cache: Cache,
    private val characterMapper: CharacterMapper,
    private val chracterSnippetMapper: CharacterSnippetMapper,
    private val itemMapper: ItemMapper
) {
    fun createCharacter(baseStats: BaseStats) =
        cache.createCharacter(characterMapper.toEntity(Character.new(baseStats)))

    fun observeActiveCharacter(): Observable<Character> = Observable.combineLatest(
        cache.getCharacter(),
        cache.getItemsForOwner().map { list -> list.map { itemMapper.fromEntity(it) } },
        { character: CharacterEntity, items: List<Item> ->
            characterMapper.fromEntity(character, items)
        }
    )
        .doOnNext { Timber.v("activeCharacter(): $it") }

    fun getCharacterSnippets() = cache.getCharacters()
        .map { characters -> characters.map { chracterSnippetMapper.fromEntity(it) } }

    fun setActiveCharacterId(characterId: Long) = cache.getCharacter(characterId)
        .firstOrError()
        .doOnSuccess { cache.setActiveCharacterId(characterId) }
        .ignoreElement()

    fun clearActiveCharacter(): Completable {
        return Completable.complete()
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
    )

    fun getItem(itemId: Long): Maybe<Item> = cache.getItemById(itemId)
        .map { itemMapper.fromEntity(it) }

    fun equipItem(itemId: Long, slot: Item.Slot): Completable {
        return cache.getCharacter()
            .firstOrError()
            .flatMapCompletable { character ->
                when (slot) {
                    Item.Slot.PRIMARY -> character.primary = itemId
                    Item.Slot.SECONDARY -> character.secondary = itemId
                    Item.Slot.TERTIARY -> character.tertiary = itemId
//                Item.Slot.GRENADE -> character.grenade = itemId
                    Item.Slot.ARMOR -> character.armor = itemId
                    Item.Slot.CLOTHING -> character.clothes = itemId
                }
                cache.updateCharacter(character)
            }
    }

    fun unequipItem(slot: Item.Slot) = cache.getCharacter()
        .firstOrError()
        .flatMapCompletable { character ->
            when (slot) {
                Item.Slot.PRIMARY -> character.primary = -1
                Item.Slot.SECONDARY -> character.secondary = -1
                Item.Slot.TERTIARY -> character.tertiary = -1
                Item.Slot.ARMOR -> character.armor = -1
                Item.Slot.CLOTHING -> character.clothes = -1
            }
            cache.updateCharacter(character)
        }

    fun updateItem(item: Item) = cache.getItemById(item.itemId)
        .flatMapCompletable { cache.updateItem(itemMapper.toEntity(item, listOf(it.ownerId))) }

    fun deleteItem(itemId: Long, money: Int) = cache.deleteItem(itemId)
        .andThen(cache.getCharacter())
        .firstOrError()
        .flatMapCompletable {
            it.money += money
            cache.updateCharacter(it)
        }

    fun updateMoney(money: Int) = cache.getCharacter()
        .firstOrError()
        .flatMapCompletable { character ->
            character.money = money
            cache.updateCharacter(character)
        }

    fun updateCharacter(baseStats: BaseStats) = cache.getCharacter()
        .firstOrError()
        .flatMapCompletable { character ->
            character.name = baseStats.name
            character.species = baseStats.species.name
            character.luck = baseStats.luck
            character.wounds = baseStats.wounds
            character.str = baseStats.str.value
            character.dex = baseStats.dex.value
            character.vit = baseStats.vit.value
            character.inl = baseStats.inl.value
            character.wis = baseStats.wis.value
            character.cha = baseStats.cha.value

            cache.updateCharacter(character)
        }
}