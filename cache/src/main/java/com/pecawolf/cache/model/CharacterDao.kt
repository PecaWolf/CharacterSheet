package com.pecawolf.cache.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

@Dao
interface CharacterDao {

    @Query("SELECT * FROM CharacterEntity")
    fun getAll(): Single<List<CharacterEntity>>

    @Query("SELECT * FROM CharacterEntity WHERE characterId IN (:characterIds)")
    fun getAllByIds(characterIds: Array<Long>): Observable<List<CharacterEntity>>

    @Insert
    fun insert(character: CharacterEntity): Single<Long>

    @Update
    fun update(character: CharacterEntity): Completable

    @Delete
    fun delete(character: CharacterEntity): Completable
}