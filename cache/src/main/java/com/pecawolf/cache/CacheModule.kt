package com.pecawolf.cache

import androidx.room.Room
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object CacheModule {
    val instance = module {
        single { Cache(get(), get()) }
        single { ApplicationPreferences(get()) }
        single {
            Room.databaseBuilder(
                get(),
                AppDatabase::class.java,
                "sport_results"
            ).build()
        }
    }

    fun start() {
        loadKoinModules(instance)
    }
}