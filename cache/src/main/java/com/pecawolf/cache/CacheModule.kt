package com.pecawolf.cache

import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object CacheModule {
    val instance = module {

    }

    fun start() {
        loadKoinModules(instance)
    }
}