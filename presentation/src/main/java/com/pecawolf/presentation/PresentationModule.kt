package com.pecawolf.presentation

import com.pecawolf.domain.DomainModule
import com.pecawolf.model.BaseStats
import com.pecawolf.model.Item
import com.pecawolf.presentation.viewmodel.ItemDetailViewModel
import com.pecawolf.presentation.viewmodel.character.BaseStatsViewModel
import com.pecawolf.presentation.viewmodel.character.CharacterSelectionViewModel
import com.pecawolf.presentation.viewmodel.character.SpeciesSelectionViewModel
import com.pecawolf.presentation.viewmodel.main.HomeViewModel
import com.pecawolf.presentation.viewmodel.main.InventoryViewModel
import com.pecawolf.presentation.viewmodel.main.LoadoutViewModel
import com.pecawolf.presentation.viewmodel.main.MainViewModel
import com.pecawolf.presentation.viewmodel.main.NewItemStep1ViewModel
import com.pecawolf.presentation.viewmodel.main.NewItemStep2ViewModel
import com.pecawolf.presentation.viewmodel.main.OtherViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object PresentationModule {

    val instance = module {
        viewModel { MainViewModel(get(), get()) }

        viewModel { HomeViewModel(get()) }

        viewModel { LoadoutViewModel(get()) }

        viewModel { InventoryViewModel(get(), get(), get()) }
        viewModel { (itemId: Long) -> ItemDetailViewModel(itemId, get(), get(), get()) }
        viewModel { NewItemStep1ViewModel() }
        viewModel { (name: String, description: String, type: Item.ItemType) ->
            NewItemStep2ViewModel(
                name,
                description,
                type,
                get()
            )
        }

        viewModel { OtherViewModel(get()) }

        viewModel { CharacterSelectionViewModel(get(), get(), get()) }
        viewModel { SpeciesSelectionViewModel() }
        viewModel { (world: BaseStats.World, species: BaseStats.Species) ->
            BaseStatsViewModel(
                world,
                species,
                get()
            )
        }
    }

    fun start() {
        DomainModule.start()
        loadKoinModules(instance)
    }
}