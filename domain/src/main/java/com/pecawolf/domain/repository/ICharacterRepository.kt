package com.pecawolf.domain.repository

import com.pecawolf.model.BaseStats
import com.pecawolf.model.Character
import com.pecawolf.model.CharacterSnippet
import com.pecawolf.model.Item
import com.pecawolf.model.Rollable
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface ICharacterRepository {
    fun createCharacter(baseStats: BaseStats): Completable

    fun observeActiveCharacter(): Observable<Character>

    fun getCharacterSnippets(): Single<List<CharacterSnippet>>

    fun setActiveCharacterId(characterId: Long): Completable

    fun clearActiveCharacter(): Completable

    fun createItemForActiveCharacter(
        name: String,
        description: String,
        type: Item.ItemType,
        loadouts: List<Item.LoadoutType>,
        damage: Item.Damage,
        wield: Item.Weapon.Wield?,
        magazineSize: Int,
        rateOfFire: Int,
        damageTypes: MutableSet<Item.DamageType>,
    ): Single<Long>

    fun getItem(itemId: Long): Maybe<Item>

    fun equipItem(itemId: Long, slot: Item.Slot): Completable

    fun unequipItem(slot: Item.Slot): Completable

    fun updateItem(item: Item): Completable

    fun deleteItem(itemId: Long, money: Int): Completable

    fun updateMoney(money: Int): Completable

    fun updateCharacter(baseStats: BaseStats): Completable

    fun updateSkill(skill: Rollable.Skill): Completable
}