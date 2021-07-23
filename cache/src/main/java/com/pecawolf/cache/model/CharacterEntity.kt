package com.pecawolf.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CharacterEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "characterId")
    var characterId: Long,
    var name: String,
    var species: String,
    var world: String,
    var luck: Int,
    var wounds: Int,
    var str: Int,
    var dex: Int,
    var vit: Int,
    var inl: Int,
    var wis: Int,
    var cha: Int,
    var money: Int,
    var primary: Long,
    var secondary: Long,
    var tertiary: Long,
    var clothes: Long,
    var armor: Long,
    val skills: MutableMap<String, Int> = mutableMapOf(),
)