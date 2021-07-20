package com.pecawolf.domain.interactor

import com.pecawolf.data.CharacterRepository
import com.pecawolf.model.Item

class GetItemDetailInteractor(private val repository: CharacterRepository) :
    MaybeInteractor<Long, Item>() {
    override fun execute(itemId: Long) = repository.getItem(itemId)
}
