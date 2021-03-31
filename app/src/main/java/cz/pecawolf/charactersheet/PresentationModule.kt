package cz.pecawolf.charactersheet

import cz.pecawolf.charactersheet.presentation.HomeViewModel
import cz.pecawolf.charactersheet.presentation.InventoryViewModel
import cz.pecawolf.charactersheet.presentation.MainViewModel
import cz.pecawolf.charactersheet.presentation.OtherViewModel
import cz.pecawolf.charactersheet.presentation.SkillsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { MainViewModel() }
    viewModel { HomeViewModel(get()) }
    viewModel { InventoryViewModel(get()) }
    viewModel { SkillsViewModel(get()) }
    viewModel { OtherViewModel(get()) }
}