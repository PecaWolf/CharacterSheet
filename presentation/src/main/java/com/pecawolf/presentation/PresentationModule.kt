package com.pecawolf.presentation

import com.pecawolf.domain.DomainModule
import cz.pecawolf.charactersheet.presentation.HomeViewModel
import cz.pecawolf.charactersheet.presentation.InventoryViewModel
import cz.pecawolf.charactersheet.presentation.MainViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object PresentationModule {

    val instance = module {
        viewModel { MainViewModel() }
        viewModel { HomeViewModel(get()) }
        viewModel { InventoryViewModel(get()) }
    }

    fun start() {
        DomainModule.start()
        loadKoinModules(instance)
    }
}