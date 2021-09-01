package com.pecawolf.domain.interactor

import com.pecawolf.domain.repository.ICharacterRepository
import com.pecawolf.model.Item

class GetItemDetailInteractor(private val repository: ICharacterRepository) :
    MaybeInteractor<Long, Item>() {
    override fun execute(itemId: Long) = repository.getItem(itemId)
}
