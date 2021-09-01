package com.pecawolf.domain.interactor

import com.pecawolf.domain.repository.ICharacterRepository
import io.reactivex.rxjava3.core.Completable

class UpdateMoneyInteractor(private val repository: ICharacterRepository) :
    CompletableInteractor<Int>() {
    override fun execute(money: Int): Completable {
        return repository.updateMoney(money)
    }
}
