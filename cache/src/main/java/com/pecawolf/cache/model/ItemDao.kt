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
interface ItemDao {

    @Query("SELECT * FROM ItemEntity")
    fun getAll(): Single<List<ItemEntity>>

    @Query("SELECT * FROM ItemEntity WHERE itemId IN (:itemIds)")
    fun getAllByIds(itemIds: Array<Long>): Single<List<ItemEntity>>

    @Query("SELECT * FROM ItemEntity WHERE ownerId IS (:ownerId)")
    fun getAllByOwnerId(ownerId: Long): Observable<List<ItemEntity>>

    @Insert
    fun insert(item: ItemEntity): Single<Long>

    @Update
    fun update(item: ItemEntity): Completable

    @Delete
    fun delete(item: ItemEntity): Completable
}