package com.pecawolf.domain.interactor

import com.pecawolf.data.CharacterRepository
import com.pecawolf.model.Item
import io.reactivex.rxjava3.core.Completable

class EquipItemInteractor(
    private val repository: CharacterRepository
) : CompletableInteractor<Pair<Long, Item.Slot>>() {

    override fun execute(params: Pair<Long, Item.Slot>): Completable =
        repository.equipItem(params.first, params.second)
}
