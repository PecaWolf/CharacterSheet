package com.pecawolf.cache.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface ItemDao {

    @Query("SELECT * FROM ItemEntity")
    fun getAll(): Single<List<ItemEntity>>

    @Query("SELECT * FROM ItemEntity WHERE itemId IN (:itemIds)")
    fun loadAllByIds(itemIds: Array<Long>): Single<List<ItemEntity>>

    @Query("SELECT * FROM ItemEntity WHERE ownerId IS (:ownerId)")
    fun loadAllByOwnerId(ownerId: Long): Single<List<ItemEntity>>

    @Insert
    fun insert(item: ItemEntity): Single<Long>

    @Delete
    fun delete(item: ItemEntity): Completable
}