package com.pecawolf.domain

import com.pecawolf.data.DataModule
import com.pecawolf.domain.interactor.ClearActiveCharacterInteractor
import com.pecawolf.domain.interactor.CreateChracterInteractor
import com.pecawolf.domain.interactor.CreateNewItemInteractor
import com.pecawolf.domain.interactor.DeleteItemInteractor
import com.pecawolf.domain.interactor.EquipItemInteractor
import com.pecawolf.domain.interactor.GetCharacterInteractor
import com.pecawolf.domain.interactor.GetCharactersInteractor
import com.pecawolf.domain.interactor.GetItemDetailInteractor
import com.pecawolf.domain.interactor.ObserveCharacterInteractor
import com.pecawolf.domain.interactor.RollDiceInteractor
import com.pecawolf.domain.interactor.SaveItemChangesInteractor
import com.pecawolf.domain.interactor.SetActiveCharacterIdInteractor
import com.pecawolf.domain.interactor.SubscribeToActiveCharacter
import com.pecawolf.domain.interactor.UnequipItemInteractor
import com.pecawolf.domain.interactor.UpdateMoneyInteractor
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object DomainModule {
    val instance = module {
        factory { CreateChracterInteractor(get()) }
        factory { GetCharacterInteractor(get()) }
        factory { ObserveCharacterInteractor(get()) }
        factory { GetCharactersInteractor(get()) }
        factory { SetActiveCharacterIdInteractor(get()) }
        factory { ClearActiveCharacterInteractor(get()) }
        factory { SubscribeToActiveCharacter(get()) }
        factory { CreateNewItemInteractor(get()) }
        factory { GetItemDetailInteractor(get()) }
        factory { SaveItemChangesInteractor(get()) }
        factory { EquipItemInteractor(get()) }
        factory { UnequipItemInteractor(get()) }
        factory { DeleteItemInteractor(get()) }
        factory { UpdateMoneyInteractor(get()) }
        factory { RollDiceInteractor(get()) }
    }

    fun start() {
        DataModule.start()
        loadKoinModules(instance)
    }
}