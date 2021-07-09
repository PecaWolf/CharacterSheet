package com.pecawolf.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CharacterSnippetEntity(
    @PrimaryKey val characterId: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "species") val species: String
)