package com.pecawolf.domain.interactor

import com.pecawolf.data.CharacterRepository
import com.pecawolf.model.Rollable

class UpdateSkillInteractor(private val repository: CharacterRepository) :
    CompletableInteractor<Rollable.Skill>() {
    override fun execute(skill: Rollable.Skill) = repository.updateSkill(skill)
}
