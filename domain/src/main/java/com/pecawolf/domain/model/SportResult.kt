package com.pecawolf.domain.model

data class SportResult(
    val title: String,
    val place: String,
    val duration: Long,
    val storage: Storage
) {
    enum class Storage {
        LOCAL, REMOTE
    }
}
