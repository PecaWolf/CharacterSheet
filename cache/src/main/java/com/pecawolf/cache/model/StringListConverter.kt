package com.pecawolf.cache.model

import androidx.room.TypeConverter

class StringListConverter {
    @TypeConverter
    fun fromString(listString: String): List<String> = listString
        .split(SEPARATOR)
        .filter { it.isNotBlank() }

    @TypeConverter
    fun toString(list: List<String>): String = list.joinToString(SEPARATOR)

    companion object {
        private const val SEPARATOR = "|"
    }
}