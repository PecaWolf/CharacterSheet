package com.pecawolf.model

import kotlin.experimental.and
import kotlin.experimental.or

data class Character(
    val base: BaseStats,
    val equipment: Equipment
) {

    interface WorldMask {
        abstract val worldMask: Byte

        val isLastRealm: Boolean
            get() = worldMask.and(LAST_REALM) == LAST_REALM

        val isDarkWay: Boolean
            get() = worldMask.and(DARK_WAY) == DARK_WAY

        val isColdFrontier: Boolean
            get() = worldMask.and(COLD_FRONTIER) == COLD_FRONTIER

        val isFantasy: Boolean
            get() = worldMask.and(FANTASY) == FANTASY

        val isSciFi: Boolean
            get() = worldMask.and(SCI_FI) == SCI_FI

        companion object {
            const val LAST_REALM: Byte = 1
            const val DARK_WAY: Byte = 2
            const val COLD_FRONTIER: Byte = 4
            const val ALL: Byte = 7
            const val FANTASY: Byte = LAST_REALM
            val SCI_FI: Byte = DARK_WAY.or(COLD_FRONTIER)
        }
    }
}