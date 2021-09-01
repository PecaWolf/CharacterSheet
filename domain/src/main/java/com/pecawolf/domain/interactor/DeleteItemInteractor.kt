package com.pecawolf.domain.interactor

import com.pecawolf.domain.repository.ICharacterRepository
import io.reactivex.rxjava3.core.Completable

class DeleteItemInteractor(val repository: ICharacterRepository) :
    CompletableInteractor<Pair<Long, Int>>() {
    override fun execute(params: Pair<Long, Int>): Completable =
        repository.deleteItem(params.first, params.second)
}