package cz.pecawolf.charactersheet.common

import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object CommonModule {

    val instance = module {
    }

    fun start() {
        loadKoinModules(instance)
    }
}