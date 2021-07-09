package com.pecawolf.presentation

import com.pecawolf.domain.DomainModule
import com.pecawolf.presentation.viewmodel.ChooseCharacterViewModel
import com.pecawolf.presentation.viewmodel.HomeViewModel
import com.pecawolf.presentation.viewmodel.InventoryViewModel
import com.pecawolf.presentation.viewmodel.MainViewModel
import com.pecawolf.presentation.viewmodel.OtherViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object PresentationModule {

    val instance = module {
        viewModel { MainViewModel() }
        viewModel { HomeViewModel(get()) }
        viewModel { InventoryViewModel(get()) }
        viewModel { OtherViewModel(get()) }
        viewModel { ChooseCharacterViewModel(get(), get()) }
    }

    fun start() {
        DomainModule.start()
        loadKoinModules(instance)
    }
}