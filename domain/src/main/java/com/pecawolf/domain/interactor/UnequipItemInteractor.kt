package com.pecawolf.domain.interactor

import com.pecawolf.domain.repository.ICharacterRepository
import com.pecawolf.model.Item
import io.reactivex.rxjava3.core.Completable

class UnequipItemInteractor(private val repository: ICharacterRepository) :
    CompletableInteractor<Item.Slot>() {
    override fun execute(slot: Item.Slot): Completable {
        return repository.unequipItem(slot)
    }
}
