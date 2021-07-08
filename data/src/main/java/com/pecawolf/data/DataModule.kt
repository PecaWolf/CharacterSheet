package com.pecawolf.data

import com.pecawolf.cache.CacheModule
import com.pecawolf.data.mapper.BaseStatsMapper
import com.pecawolf.data.mapper.CharacterMapper
import com.pecawolf.data.mapper.EquipmentMapper
import com.pecawolf.remote.RemoteModule
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object DataModule {
    val instance = module {
        single { CharacterRepository(get(), get()) }

        single { CharacterMapper(get(), get()) }
        single { BaseStatsMapper() }
        single { EquipmentMapper() }
    }

    fun start() {
        RemoteModule.start()
        CacheModule.start()
        loadKoinModules(instance)
    }
}