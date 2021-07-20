package com.pecawolf.domain.interactor

import com.pecawolf.data.CharacterRepository
import io.reactivex.rxjava3.core.Completable

class UpdateMoneyInteractor(private val repository: CharacterRepository) :
    CompletableInteractor<Int>() {
    override fun execute(money: Int): Completable {
        return repository.updateMoney(money)
    }
}
