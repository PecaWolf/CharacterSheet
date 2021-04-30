package cz.pecawolf.charactersheet.data

import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object DataModule {
    val instance = module {
        single { CharacterRepository(get()) }
    }

    fun start() {
        loadKoinModules(instance)
    }
}