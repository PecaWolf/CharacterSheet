package com.pecawolf.remote

import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object RemoteModule {
    val instance = module {

    }

    fun start() {
        loadKoinModules(instance)
    }
}