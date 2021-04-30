package cz.pecawolf.charactersheet.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import cz.pecawolf.charactersheet.domain.GetBaseStatsInteractor
import cz.pecawolf.charactersheet.domain.SetBaseStatsInteractor
import cz.pecawolf.charactersheet.domain.model.BaseStats
import cz.pecawolf.charactersheet.presentation.extensions.notifyChanged

class HomeViewModel(
    private val mainViewModel: MainViewModel,
    val getBaseStats: GetBaseStatsInteractor,
    val setBaseStats: SetBaseStatsInteractor
) : ViewModel() {

    private val _baseStats = MutableLiveData(
        BaseStats(
            "Mathias Caldera",
            BaseStats.Species.HUMAN,
            10,
            4,
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
    val luckAndHp: LiveData<String> = Transformations.map(_baseStats) { it.luckAndWounds }

    init {
        loadData()
    }

    fun onHealClicked() {
        _baseStats.value?.apply {
            if (wounds < vit) wounds++
            else luck++
            Log.d("HECK", "onHealClicked(): $luck + $wounds")
        }
        _baseStats.notifyChanged()
    }

    fun onDamageClicked() {
        _baseStats.value?.apply {
            if (luck > 0) luck--
            else wounds = maxOf(wounds - 1, 0)
            Log.d("HECK", "onDamageClicked(): $luck + $wounds")
        }
        _baseStats.notifyChanged()
    }

    fun onDamageLongClicked() {
        _baseStats.value?.apply {
            wounds = maxOf(wounds - 1, 0)
            Log.d("HECK", "onDamageLongClicked(): $luck + $wounds")
        }
        _baseStats.notifyChanged()
    }

    private fun loadData() {
        setBaseStats.setStats(baseStats.value!!)
        getBaseStats.getStats().let {

        }
    }
}