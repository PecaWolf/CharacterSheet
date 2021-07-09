package com.pecawolf.cache.model

import androidx.room.TypeConverter

class ItemTypeConverter {
    @TypeConverter
    fun fromString(name: String): ItemEntity.ItemType = ItemEntity.ItemType.valueOf(name)

    @TypeConverter
    fun toString(type: ItemEntity.ItemType): String = type.name
}