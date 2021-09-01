package com.pecawolf.data

import com.pecawolf.data.mapper.CharacterFactory
import com.pecawolf.data.mapper.CharacterSnippetDataMapper
import com.pecawolf.data.mapper.ItemDataMapper
import com.pecawolf.data.mapper.SkillDataMapper
import com.pecawolf.data.repository.CharacterRepository
import com.pecawolf.data.repository.DiceRepository
import com.pecawolf.domain.repository.ICharacterRepository
import com.pecawolf.domain.repository.IDiceRepository
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object DataModule {
    val instance = module {
        single { CharacterRepository(get(), get(), get(), get(), get()) as ICharacterRepository }
        single { DiceRepository(get()) as IDiceRepository }

        single { CharacterFactory(get(), get()) }
        single { CharacterSnippetDataMapper() }
        single { ItemDataMapper() }
        single { SkillDataMapper() }
    }

    fun start() {
        loadKoinModules(instance)
    }
}