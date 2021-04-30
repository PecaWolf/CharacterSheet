package cz.pecawolf.charactersheet.common

import cz.pecawolf.charactersheet.common.model.BaseStatsEntity

interface IRemote {
    fun foo(): String
    fun setCharacter(baseStats: BaseStatsEntity)
}