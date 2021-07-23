package com.pecawolf.cache.model

import androidx.room.TypeConverter

class StringIntMapConverter {
    @TypeConverter
    fun fromString(mapString: String): Map<String, Int> = mapString
        .split(SEPARATOR)
        .filter { it.isNotBlank() }
        .map { it.split(KEY_VALUE_SEPARATOR) }
        .filter { it.size != 2 }
        .map { Pair(it[0], it[1].toInt()) }
        .toMap()

    @TypeConverter
    fun toString(map: Map<String, Int>): String = map.entries.joinToString(SEPARATOR) {
        it.run { key + KEY_VALUE_SEPARATOR + value }
    }

    companion object {
        private const val KEY_VALUE_SEPARATOR = ":"
        private const val SEPARATOR = "|"
    }
}