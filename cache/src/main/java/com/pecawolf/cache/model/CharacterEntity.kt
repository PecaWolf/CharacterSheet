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
    var luck: Int,
    var wounds: Int,
    val str: Int,
    val dex: Int,
    val vit: Int,
    val inl: Int,
    val wis: Int,
    val cha: Int,
    val money: Int,
    val primary: Long,
    val secondary: Long,
    val tertiary: Long,
    val clothes: Long,
    val armor: Long,
    val backpack: List<Long> = listOf(),
    val storage: List<Long> = listOf(),
) {
}