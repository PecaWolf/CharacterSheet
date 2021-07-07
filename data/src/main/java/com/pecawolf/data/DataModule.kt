package com.pecawolf.data

import com.pecawolf.cache.CacheModule
import com.pecawolf.remote.RemoteModule
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object DataModule {
    val instance = module {

    }

    fun start() {
        RemoteModule.start()
        CacheModule.start()
        loadKoinModules(instance)
    }
}