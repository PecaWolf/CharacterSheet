package com.pecawolf.cache

import androidx.room.Room
import com.pecawolf.cache.mapper.CharacterEntityMapper
import com.pecawolf.cache.mapper.ItemEntityMapper
import com.pecawolf.data.datasource.ICache
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object CacheModule {
    val instance = module {
        single { Cache(get(), get(), get(), get()) as ICache }
        single { ApplicationPreferences(get()) }
        single {
            Room.databaseBuilder(
                get(),
                AppDatabase::class.java,
                "local_characters"
            ).build()
        }
        single { CharacterEntityMapper() }
        single { ItemEntityMapper() }
    }

    fun start() {
        loadKoinModules(instance)
    }
}