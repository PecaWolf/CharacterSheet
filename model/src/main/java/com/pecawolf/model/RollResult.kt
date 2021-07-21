package com.pecawolf.model

sealed class RollResult(open val roll: Int) {
    object CriticalSuccess : RollResult(1)
    data class Success(override val roll: Int) : RollResult(roll)
    data class Failure(override val roll: Int) : RollResult(roll)
    object CriticalFailure : RollResult(20)

    val isSuccess: Boolean
        get() = this is CriticalSuccess || this is Success
}
