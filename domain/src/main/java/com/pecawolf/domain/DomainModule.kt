package com.pecawolf.domain

import com.pecawolf.data.DataModule
import com.pecawolf.domain.interactor.ClearActiveCharacterInteractor
import com.pecawolf.domain.interactor.CreateChracterInteractor
import com.pecawolf.domain.interactor.GetCharacterInteractor
import com.pecawolf.domain.interactor.GetCharactersInteractor
import com.pecawolf.domain.interactor.SetActiveCharacterIdInteractor
import com.pecawolf.domain.interactor.SubscribeToActiveCharacter
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object DomainModule {
    val instance = module {
        factory { CreateChracterInteractor(get()) }
        factory { GetCharacterInteractor(get()) }
        factory { GetCharactersInteractor(get()) }
        factory { SetActiveCharacterIdInteractor(get()) }
        factory { ClearActiveCharacterInteractor(get()) }
        factory { SubscribeToActiveCharacter(get()) }
    }

    fun start() {
        DataModule.start()
        loadKoinModules(instance)
    }
}