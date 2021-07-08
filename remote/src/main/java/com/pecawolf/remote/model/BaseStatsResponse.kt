package com.pecawolf.remote.model

data class BaseStatsResponse(
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
    val money: Int
)