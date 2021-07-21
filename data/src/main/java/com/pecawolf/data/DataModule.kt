package com.pecawolf.data

import com.pecawolf.cache.CacheModule
import com.pecawolf.data.mapper.CharacterMapper
import com.pecawolf.data.mapper.CharacterSnippetMapper
import com.pecawolf.data.mapper.ItemMapper
import com.pecawolf.remote.RemoteModule
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object DataModule {
    val instance = module {
        single { CharacterRepository(get(), get(), get(), get()) }
        single { DiceRepository(get(), get()) }

        single { CharacterSnippetMapper() } // TODO: make it factory probably
        single { CharacterMapper() } // TODO: make it factory probably
        single { ItemMapper() } // TODO: make it factory probably
    }

    fun start() {
        RemoteModule.start()
        CacheModule.start()
        loadKoinModules(instance)
    }
}