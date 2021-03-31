package cz.pecawolf.charactersheet.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cz.pecawolf.charactersheet.common.model.BaseStats

class HomeViewModel(val mainViewModel: MainViewModel) : ViewModel() {

    private val _baseStats = MutableLiveData<BaseStats>(
        BaseStats(
            "Mathias Caldera",
            BaseStats.Species.HUMAN,
            10,
            3,
            5,
            6,
            4,
            7,
            6,
            8,
            1000
        )
    )
    val baseStats: LiveData<BaseStats> = _baseStats
}