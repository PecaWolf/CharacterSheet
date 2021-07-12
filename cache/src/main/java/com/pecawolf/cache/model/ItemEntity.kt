package com.pecawolf.cache.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ItemEntity(
    @PrimaryKey(autoGenerate = true)
    val itemId: Long,
    val ownerId: Long,
    val type: ItemType,
    val name: String,
    val description: String,
    val allowedLoadouts: List<String>,
    val enhancements: List<String>,
    val damage: String?,
    val wield: String?,
    val damageTypes: List<String>?,
    val magazine: Int?,
    val rateOfFire: Int?,
    val protections: List<String>?,
) {
    enum class ItemType {
        BARE_HANDS,
        KNIFE,
        SWORD,
        AXE,
        BOW,
        CROSSBOW,
        PISTOL,
        REVOLVER,
        RIFLE,
        SUBMACHINE_GUN,
        SHOTGUN,
        MACHINE_GUN,
        ANTIMATERIAL_GUN,
        NONE,
        CLOTHING,
        KEVLAR,
        EXO_SKELETON,
        VAC_SUIT,
        VAC_ARMOR,
        POWERED_ARMOR,
        OTHER
    }
}