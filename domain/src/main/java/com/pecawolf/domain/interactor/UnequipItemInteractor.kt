package com.pecawolf.domain.interactor

import com.pecawolf.data.CharacterRepository
import com.pecawolf.model.Item
import io.reactivex.rxjava3.core.Completable

class UnequipItemInteractor(private val repository: CharacterRepository) :
    CompletableInteractor<Item.Slot>() {
    override fun execute(slot: Item.Slot): Completable {
        return repository.unequipItem(slot)
    }
}
