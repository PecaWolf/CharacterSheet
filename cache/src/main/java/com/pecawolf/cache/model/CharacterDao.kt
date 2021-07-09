package com.pecawolf.cache.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface CharacterDao {

    @Query("SELECT * FROM CharacterEntity")
    fun getAll(): List<CharacterEntity>

    @Query("SELECT * FROM CharacterEntity WHERE characterId IN (:characterIds)")
    fun loadAllByIds(characterIds: Array<String>): Single<List<CharacterEntity>>

    @Insert
    fun insertAll(vararg CharacterEntitys: CharacterEntity): Completable

    @Delete
    fun delete(character: CharacterEntity): Completable
}