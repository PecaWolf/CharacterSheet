package com.pecawolf.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pecawolf.cache.model.CharacterDao
import com.pecawolf.cache.model.CharacterEntity
import com.pecawolf.cache.model.LongListConverter

@Database(entities = arrayOf(CharacterEntity::class), version = 1)
@TypeConverters(LongListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
}