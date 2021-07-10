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
    fun getAll(): Single<List<CharacterEntity>>

    @Query("SELECT * FROM CharacterEntity WHERE characterId IN (:characterIds)")
    fun loadAllByIds(characterIds: Array<Long>): Single<List<CharacterEntity>>

    @Insert
    fun insert(characterEntity: CharacterEntity): Single<Long>

    @Delete
    fun delete(character: CharacterEntity): Completable
}