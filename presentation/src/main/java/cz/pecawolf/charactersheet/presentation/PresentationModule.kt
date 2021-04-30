package cz.pecawolf.charactersheet.presentation

import cz.pecawolf.charactersheet.domain.DomainModule
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object PresentationModule {
    val instance = module {
        viewModel { MainViewModel() }
        viewModel { HomeViewModel(get(), get(), get()) }
        viewModel { InventoryViewModel(get()) }
        viewModel { SkillsViewModel(get()) }
        viewModel { OtherViewModel(get()) }
    }

    fun start() {
        DomainModule.start()
        loadKoinModules(instance)
    }
}