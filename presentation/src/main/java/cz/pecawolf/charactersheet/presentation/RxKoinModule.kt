package cz.pecawolf.charactersheet.presentation

import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object RxKoinModule {
    val instance = module {
        single<SchedulerProvider> { SchedulerProviderImpl() }
    }

    fun start() {
        loadKoinModules(instance)
    }
}