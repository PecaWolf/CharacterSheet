package com.pecawolf.domain.interactor

import com.pecawolf.data.CharacterRepository
import com.pecawolf.model.Item

class SaveItemChanges(private val repository: CharacterRepository) :
    CompletableInteractor<Item>() {

    override fun execute(item: Item) = repository.updateItem(item)
}