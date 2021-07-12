package com.pecawolf.presentation.viewmodel.character

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.pecawolf.model.BaseStats
import com.pecawolf.presentation.extensions.SingleLiveEvent
import com.pecawolf.presentation.extensions.notifyChanged
import com.pecawolf.presentation.viewmodel.BaseViewModel

class SpeciesSelectionViewModel : BaseViewModel() {

    private val _isContinueEnabled = MutableLiveData<Boolean>(false)
    private val _species = MutableLiveData<List<BaseStats.Species>>()
    private val _worlds = MutableLiveData<List<BaseStats.World>>()
    private val _navigateToNextStep = SingleLiveEvent<Pair<BaseStats.World, BaseStats.Species>>()

    private var selectedSpecies: BaseStats.Species? = null
        set(value) {
            field = value
            _isContinueEnabled.value = selectedSpecies != null && selectedWorld != null
        }
    private var selectedWorld: BaseStats.World? = null
        set(value) {
            field = value
            _isContinueEnabled.value = selectedSpecies != null && selectedWorld != null
        }

    val worlds: LiveData<List<Pair<BaseStats.World, Boolean>>> =
        Transformations.map(_worlds) { worlds ->
            worlds.map { Pair(it, it == selectedWorld) }
        }

    val species: LiveData<List<Pair<BaseStats.Species, Boolean>>> =
        Transformations.map(_species) { species ->
            species.map { Pair(it, it == selectedSpecies) }
        }

    val isContinueEnabled: LiveData<Boolean> = _isContinueEnabled
    val navigateToNextStep: LiveData<Pair<BaseStats.World, BaseStats.Species>> = _navigateToNextStep

    init {
        onRefresh()
    }

    override fun onRefresh() {
        _worlds.value = BaseStats.World.values().toList()
        _worlds.notifyChanged()
    }

    fun onWorldSelected(world: BaseStats.World) {
        selectedWorld = world
        if (!world.species.contains(selectedSpecies)) selectedSpecies = null
        _worlds.notifyChanged()
        _species.value = world.species
    }

    fun onSpeciesSelected(species: BaseStats.Species) {
        selectedSpecies = species
        _species.notifyChanged()
    }

    fun onContinueClicked() {
        _navigateToNextStep.postValue(selectedWorld!! to selectedSpecies!!)
    }
}