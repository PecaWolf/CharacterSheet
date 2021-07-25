package com.pecawolf.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class Rollable(open var value: Int) : Parcelable {

    abstract val trap: Int

    sealed class Stat(override var value: Int) : Rollable(value) {

        override val trap: Int
            get() = value * 2

        @Parcelize
        data class Strength(override var value: Int) : Stat(value)

        @Parcelize
        data class Dexterity(override var value: Int) : Stat(value)

        @Parcelize
        data class Vitality(override var value: Int) : Stat(value)

        @Parcelize
        data class Intelligence(override var value: Int) : Stat(value)

        @Parcelize
        data class Wisdom(override var value: Int) : Stat(value)

        @Parcelize
        data class Charisma(override var value: Int) : Stat(value)
    }

    @Parcelize
    data class Skill(
        val code: String,
        val name: String,
        val stat: Stat,
        override var value: Int,
    ) : Rollable(value) {

        override val trap: Int
            get() = stat.value + value * 3
    }
}
