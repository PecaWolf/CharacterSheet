package cz.pecawolf.charactersheet.data

import cz.pecawolf.charactersheet.common.IRemote
import cz.pecawolf.charactersheet.common.model.BaseStats
import io.reactivex.rxjava3.core.Single

class CharacterRepository(private val remote: IRemote) {

    private var currentCharacterId: String? = null

    fun setCharacter(baseStats: BaseStats) {
        remote.setCharacter(currentCharacterId, baseStats)
    }

    fun getCharacter(characterId: String): Single<BaseStats> {
        return remote.getCharacter(characterId)
            .doOnEvent { _, _ -> currentCharacterId = characterId }
    }
}