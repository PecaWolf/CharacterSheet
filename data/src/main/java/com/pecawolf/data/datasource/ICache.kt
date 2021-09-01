package com.pecawolf.data.datasource

import com.pecawolf.data.model.CharacterData
import com.pecawolf.data.model.CharacterSnippetData
import com.pecawolf.data.model.ItemData
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface ICache {

    fun createCharacter(character: CharacterData): Completable

    fun updateCharacter(character: CharacterData): Completable

    fun getCharacters(): Single<List<CharacterSnippetData>>

    fun setActiveCharacterId(characterId: Long?)

    fun getCharacter(characterId: Long? = null): Observable<CharacterData>

    fun getItemsForOwner(ownerId: Long? = null): Observable<List<ItemData>>

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
        ownerId: Long? = null,
    ): Single<Long>

    fun getItemById(itemId: Long): Maybe<ItemData>

    fun updateItem(item: ItemData): Completable

    fun deleteItem(itemId: Long): Completable
}