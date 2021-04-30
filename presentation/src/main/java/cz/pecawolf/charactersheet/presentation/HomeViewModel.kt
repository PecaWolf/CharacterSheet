package cz.pecawolf.charactersheet.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import cz.pecawolf.charactersheet.common.model.BaseStats
import cz.pecawolf.charactersheet.domain.GetBaseStatsInteractor
import cz.pecawolf.charactersheet.domain.SetBaseStatsInteractor
import cz.pecawolf.charactersheet.presentation.extensions.notifyChanged

class HomeViewModel(
    private val mainViewModel: MainViewModel,
    val getBaseStats: GetBaseStatsInteractor,
    val setBaseStats: SetBaseStatsInteractor
) : BaseViewModel() {

    private val _baseStats = MutableLiveData<BaseStats>()
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

        saveChanges()
    }

    fun onDamageClicked() {
        _baseStats.value?.apply {
            if (luck > 0) luck--
            else wounds = maxOf(wounds - 1, 0)
            Log.d("HECK", "onDamageClicked(): $luck + $wounds")
        }
        _baseStats.notifyChanged()

        saveChanges()
    }

    fun onDamageLongClicked() {
        _baseStats.value?.apply {
            wounds = maxOf(wounds - 1, 0)
            Log.d("HECK", "onDamageLongClicked(): $luck + $wounds")
        }
        _baseStats.notifyChanged()

        saveChanges()
    }

    private fun saveChanges() {
        setBaseStats.setStats(_baseStats.value!!)
    }

    private fun loadData() {
        getBaseStats.getStats("VPTNEoSmGeDCBXNhlCNW")
            .observe(
                {
                    Log.d("HECKERY", "onLoadData(): ${it.name}")
                    _baseStats.value = it
                },
                { Log.e("HECKERY", "onLoadDataError(): ", it) }
            )
    }
}