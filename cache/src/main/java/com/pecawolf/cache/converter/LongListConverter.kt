package com.pecawolf.cache.converter

import androidx.room.TypeConverter

class LongListConverter {
    @TypeConverter
    fun fromString(listString: String): List<Long> = listString
        .split(SEPARATOR)
        .mapNotNull { it.toLongOrNull() }

    @TypeConverter
    fun toString(list: List<Long>): String = list.joinToString(SEPARATOR)

    companion object {
        private const val SEPARATOR = "|"
    }
}