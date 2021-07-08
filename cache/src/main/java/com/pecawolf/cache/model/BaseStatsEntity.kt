package com.pecawolf.cache.model

data class BaseStatsEntity(
    val name: String,
    val species: Species,
    var luck: Int,
    var wounds: Int,
    val str: Int,
    val dex: Int,
    val vit: Int,
    val inl: Int,
    val wis: Int,
    val cha: Int,
    val money: Int
) {

    val luckAndWounds: Pair<Int, Int>
        get() = luck to wounds

    val strength: String
        get() = "$str"
    val strengthTrap: String
        get() = "${str * 2}"
    val dexterity: String
        get() = "$dex"
    val dexterityTrap: String
        get() = "${dex * 2}"
    val vitality: String
        get() = "$vit"
    val vitalityTrap: String
        get() = "${vit * 2}"
    val inteligence: String
        get() = "$inl"
    val inteligenceTrap: String
        get() = "${inl * 2}"
    val wisdom: String
        get() = "$wis"
    val wisdomTrap: String
        get() = "${wis * 2}"
    val charisma: String
        get() = "$cha"
    val charismaTrap: String
        get() = "${cha * 2}"

    enum class Species(
        val standardName: String,
        val worldMask: Byte
    )
}