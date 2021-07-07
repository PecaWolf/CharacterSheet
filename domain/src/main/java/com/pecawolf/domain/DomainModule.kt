package com.pecawolf.domain

import com.pecawolf.data.DataModule
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object DomainModule {
    val instance = module {

    }

    fun start() {
        DataModule.start()
        loadKoinModules(instance)
    }
}