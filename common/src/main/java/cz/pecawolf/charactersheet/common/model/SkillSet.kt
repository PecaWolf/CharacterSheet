package cz.pecawolf.charactersheet.common.model

class SkillSet(
    val strSkills: List<Skill> = listOf(),
    val dexSkills: List<Skill> = listOf(),
    val vitSkills: List<Skill> = listOf(),
    val inlSkills: List<Skill> = listOf(),
    val wisSkills: List<Skill> = listOf(),
    val chaSkills: List<Skill> = listOf()
) {
    class Skill(
        val name: String,
        val level: Int,
        val stat: Stat
    )

    enum class Stat(val id: Int) {
        STR(0),
        DEX(1),
        VIT(2),
        INL(3),
        WIS(4),
        CHA(5)
    }
}