package com.pecawolf.domain.interactor

import com.pecawolf.data.CharacterRepository
import com.pecawolf.model.BaseStats
import io.reactivex.rxjava3.core.Completable

class UpdateCharacterInteractor(private val repository: CharacterRepository) :
    CompletableInteractor<BaseStats>() {
    override fun execute(baseStats: BaseStats): Completable = repository.updateCharacter(baseStats)
}
