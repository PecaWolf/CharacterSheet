package com.pecawolf.domain.interactor

import com.pecawolf.domain.repository.ICharacterRepository
import com.pecawolf.model.Item

class SaveItemChangesInteractor(private val repository: ICharacterRepository) :
    CompletableInteractor<Item>() {

    override fun execute(item: Item) = repository.updateItem(item)
}
