package com.pecawolf.domain.interactor

import com.pecawolf.domain.repository.ICharacterRepository
import com.pecawolf.model.BaseStats
import io.reactivex.rxjava3.core.Completable

class UpdateCharacterInteractor(private val repository: ICharacterRepository) :
    CompletableInteractor<BaseStats>() {
    override fun execute(baseStats: BaseStats): Completable = repository.updateCharacter(baseStats)
}
