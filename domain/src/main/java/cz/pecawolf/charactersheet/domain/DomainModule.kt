package cz.pecawolf.charactersheet.domain

import cz.pecawolf.charactersheet.data.DataModule
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object DomainModule {
    val instance = module {
        factory { GetBaseStatsInteractor(get()) }
        factory { SetBaseStatsInteractor(get()) }
    }

    fun start() {
        DataModule.start()
        loadKoinModules(instance)
    }
}