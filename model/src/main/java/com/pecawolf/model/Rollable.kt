package com.pecawolf.model

sealed class Rollable(open var value: Int) {

    abstract val trap: Int

    sealed class Stat(override var value: Int) : Rollable(value) {

        override val trap: Int
            get() = value * 2

        data class Strength(override var value: Int) : Stat(value)
        data class Dexterity(override var value: Int) : Stat(value)
        data class Vitality(override var value: Int) : Stat(value)
        data class Intelligence(override var value: Int) : Stat(value)
        data class Wisdom(override var value: Int) : Stat(value)
        data class Charisma(override var value: Int) : Stat(value)

        enum class Enum {
            STR, DEX, VIT, INL, WIS, CHA,
        }
    }

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
