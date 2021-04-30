package cz.pecawolf.charactersheet.common

object Rules {

    fun getCharacterStatTrap(value: Int): Int {
        return value * CHARACTER_STAT_TRAP_MULTIPLIER
    }

    fun getSkillTrap(stat: Int, skill: Int): Int {
        return stat + skill * SKILL_TRAP_MULTIPLIER
    }

    const val CHARACTER_STAT_TRAP_MULTIPLIER = 2
    const val SKILL_TRAP_MULTIPLIER = 3

}