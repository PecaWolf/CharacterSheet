package com.pecawolf.cache.converter

import androidx.room.TypeConverter

class StringIntMapConverter {
    @TypeConverter
    fun fromString(mapString: String): MutableMap<String, Int> = mapString
        .split(SEPARATOR)
        .filter { it.isNotBlank() }
        .map { it.split(KEY_VALUE_SEPARATOR) }
        .filter { it.size == 2 }
        .map { Pair(it[0], it[1].toInt()) }
        .toMap()
        .toMutableMap()

    @TypeConverter
    fun toString(map: MutableMap<String, Int>): String {
        return map.entries.joinToString(SEPARATOR) {
            it.run { key + KEY_VALUE_SEPARATOR + value }
        }
    }

    companion object {
        private const val KEY_VALUE_SEPARATOR = ":"
        private const val SEPARATOR = "|"
    }
}