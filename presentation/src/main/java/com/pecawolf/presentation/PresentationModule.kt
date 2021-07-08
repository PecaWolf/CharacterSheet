package com.pecawolf.presentation

import com.pecawolf.domain.DomainModule
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