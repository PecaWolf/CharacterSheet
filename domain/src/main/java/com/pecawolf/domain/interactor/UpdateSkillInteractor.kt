package com.pecawolf.domain.interactor

import com.pecawolf.domain.repository.ICharacterRepository
import com.pecawolf.model.Rollable

class UpdateSkillInteractor(private val repository: ICharacterRepository) :
    CompletableInteractor<Rollable.Skill>() {
    override fun execute(skill: Rollable.Skill) = repository.updateSkill(skill)
}
