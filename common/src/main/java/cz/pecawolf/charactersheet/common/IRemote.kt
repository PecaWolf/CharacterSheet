package cz.pecawolf.charactersheet.common

import cz.pecawolf.charactersheet.common.model.BaseStats
import io.reactivex.rxjava3.core.Single

interface IRemote {
    fun getCharacter(id: String): Single<BaseStats>
    fun setCharacter(id: String?, baseStats: BaseStats)
}