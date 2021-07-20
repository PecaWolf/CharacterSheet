package com.pecawolf.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CharacterEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "characterId")
    val characterId: Long,
    val name: String,
    val species: String,
    val world: String,
    var luck: Int,
    var wounds: Int,
    val str: Int,
    val dex: Int,
    val vit: Int,
    val inl: Int,
    val wis: Int,
    val cha: Int,
    var money: Int,
    var primary: Long,
    var secondary: Long,
    var tertiary: Long,
    var clothes: Long,
    var armor: Long,
    val backpack: MutableList<Long> = mutableListOf(),
)