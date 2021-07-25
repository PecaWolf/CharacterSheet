package com.pecawolf.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pecawolf.cache.model.CharacterDao
import com.pecawolf.cache.model.CharacterEntity
import com.pecawolf.cache.model.ItemDao
import com.pecawolf.cache.model.ItemEntity
import com.pecawolf.cache.model.LongListConverter
import com.pecawolf.cache.model.StringIntMapConverter
import com.pecawolf.cache.model.StringListConverter

@Database(
    entities = arrayOf(
        CharacterEntity::class,
        ItemEntity::class
    ), version = 1
)
@TypeConverters(
    LongListConverter::class,
    StringListConverter::class,
    StringIntMapConverter::class,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
    abstract fun itemDao(): ItemDao
}