package cz.pecawolf.charactersheet.data

import cz.pecawolf.charactersheet.common.IRemote
import cz.pecawolf.charactersheet.common.model.BaseStatsEntity
import java.util.Random

class CharacterRepository(private val remote: IRemote) {

    fun setCharacter(baseStats: BaseStatsEntity) {
        remote.setCharacter(baseStats)
    }

    fun getCharacter(): String {
        return remote.foo()
    }

    private fun random(): String? {
        val generator = Random()
        val randomStringBuilder = StringBuilder()
        val randomLength: Int = generator.nextInt(MAX_LENGTH)
        var tempChar: Char
        for (i in 0 until randomLength) {
            tempChar = (generator.nextInt(96) + 32).toChar()
            randomStringBuilder.append(tempChar)
        }
        return randomStringBuilder.toString()
    }

    companion object {
        private const val MAX_LENGTH = 15
    }
}