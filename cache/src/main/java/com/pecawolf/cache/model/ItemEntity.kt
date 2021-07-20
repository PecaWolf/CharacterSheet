package com.pecawolf.cache.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ItemEntity(
    @PrimaryKey(autoGenerate = true)
    val itemId: Long,
    val ownerId: Long,
    val type: String,
    val name: String,
    val description: String,
    val count: Int,
    val allowedLoadouts: List<String>,

    val enhancements: List<String>,

    val damage: String,

    val wield: String,
    val damageTypes: List<String>,
    val magazine: Int,
    val rateOfFire: Int,
)