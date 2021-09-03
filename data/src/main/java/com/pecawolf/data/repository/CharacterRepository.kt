package com.pecawolf.data.repository

import com.pecawolf.data.datasource.ICache
import com.pecawolf.data.datasource.ISkillsRemote
import com.pecawolf.data.mapper.CharacterFactory
import com.pecawolf.data.mapper.CharacterSnippetDataMapper
import com.pecawolf.data.mapper.ItemDataMapper
import com.pecawolf.data.model.CharacterData
import com.pecawolf.data.model.CharacterSnippetData
import com.pecawolf.data.model.ItemData
import com.pecawolf.data.model.SkillsData
import com.pecawolf.domain.repository.ICharacterRepository
import com.pecawolf.model.BaseStats
import com.pecawolf.model.Character
import com.pecawolf.model.Item
import com.pecawolf.model.Rollable
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import timber.log.Timber

class CharacterRepository(
    private val remote: ISkillsRemote,
    private val cache: ICache,
    private val characterFactory: CharacterFactory,
    private val characterSnippetMapper: CharacterSnippetDataMapper,
    private val itemMapper: ItemDataMapper,
) : ICharacterRepository {

    override fun createCharacter(baseStats: BaseStats) =
        cache.createCharacter(CharacterData.new(baseStats))

    override fun observeActiveCharacter(): Observable<Character> = Observable.combineLatest(
        cache.getCharacter(null),
        cache.getItemsForOwner(null),
        remote.observeSkills().distinctUntilChanged(),
        { character: CharacterData, items: List<ItemData>, skills: List<SkillsData> ->
            characterFactory.createCharacter(character, items, skills).also {
                Timber.d("${it.baseStats.luckAndWounds}")
            }
        }
    )
        .doOnNext { Timber.v("activeCharacter(): $it") }

    override fun getCharacterSnippets() = cache.getCharacters()
        .map { characters: List<CharacterSnippetData> ->
            characters.map { characterSnippetMapper.fromData(it) }
        }

    override fun setActiveCharacterId(characterId: Long) = cache.getCharacter(characterId)
        .firstOrError()
        .doOnSuccess { cache.setActiveCharacterId(characterId) }
        .ignoreElement()

    override fun clearActiveCharacter() = Completable.complete()
        .doOnComplete { cache.setActiveCharacterId(null) }

    override fun createItemForActiveCharacter(
        name: String,
        description: String,
        type: Item.ItemType,
        loadouts: List<Item.LoadoutType>,
        damage: Item.Damage,
        wield: Item.Weapon.Wield?,
        magazineSize: Int,
        rateOfFire: Int,
        damageTypes: MutableSet<Item.DamageType>,
    ): Single<Long> = cache.createItemForCharacter(
        name,
        description,
        type.name,
        loadouts.map { it.name },
        damage.name,
        wield?.name ?: "",
        magazineSize,
        rateOfFire,
        damageTypes.map { it.name },
    )

    override fun getItem(itemId: Long): Maybe<Item> = cache.getItemById(itemId)
        .map { itemMapper.fromData(it) }

    override fun equipItem(itemId: Long, slot: Item.Slot): Completable {
        return cache.getCharacter()
            .firstOrError()
            .flatMapCompletable { character ->
                when (slot) {
                    Item.Slot.PRIMARY -> character.primary = itemId
                    Item.Slot.SECONDARY -> character.secondary = itemId
                    Item.Slot.TERTIARY -> character.tertiary = itemId
//                Item.Slot.GRENADE -> character.grenade = itemId
                    Item.Slot.ARMOR -> character.armor = itemId
                    Item.Slot.CLOTHES -> character.clothes = itemId
                }
                cache.updateCharacter(character)
            }
    }

    override fun unequipItem(slot: Item.Slot) = cache.getCharacter()
        .firstOrError()
        .flatMapCompletable { character ->
            when (slot) {
                Item.Slot.PRIMARY -> character.primary = -1
                Item.Slot.SECONDARY -> character.secondary = -1
                Item.Slot.TERTIARY -> character.tertiary = -1
                Item.Slot.ARMOR -> character.armor = -1
                Item.Slot.CLOTHES -> character.clothes = -1
            }
            cache.updateCharacter(character)
        }

    override fun updateItem(item: Item) = cache.getItemById(item.itemId)
        .flatMapCompletable { cache.updateItem(itemMapper.toData(item, it.ownerId)) }

    override fun deleteItem(itemId: Long, money: Int) = cache.deleteItem(itemId)
        .andThen(cache.getCharacter())
        .firstOrError()
        .flatMapCompletable {
            it.money += money
            cache.updateCharacter(it)
        }

    override fun updateMoney(money: Int) = cache.getCharacter()
        .firstOrError()
        .flatMapCompletable { character ->
            character.money = money
            cache.updateCharacter(character)
        }

    override fun updateCharacter(baseStats: BaseStats) = cache.getCharacter(null)
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

    override fun updateSkill(skill: Rollable.Skill) = cache.getCharacter()
        .firstOrError()
        .flatMapCompletable { character ->
            character.skills[skill.code] = skill.value
            Timber.w("updateSkill(): ${character.skills}")
            cache.updateCharacter(character)
        }
}