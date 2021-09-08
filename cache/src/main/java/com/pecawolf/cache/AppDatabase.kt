package com.pecawolf.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pecawolf.cache.converter.LongListConverter
import com.pecawolf.cache.converter.StringIntMapConverter
import com.pecawolf.cache.converter.StringListConverter
import com.pecawolf.cache.dao.CharacterDao
import com.pecawolf.cache.dao.ItemDao
import com.pecawolf.cache.model.CharacterEntity
import com.pecawolf.cache.model.ItemEntity

@Database(
    entities = arrayOf(
        CharacterEntity::class,
        ItemEntity::class,
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