package com.pecawolf.presentation.viewmodel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.distinctUntilChanged
import com.pecawolf.model.BaseStats
import com.pecawolf.presentation.extensions.notifyChanged
import com.pecawolf.presentation.viewmodel.BaseViewModel

class HomeViewModel(private val mainViewModel: MainViewModel) : BaseViewModel() {

    private val _baseStats = MutableLiveData<BaseStats>()
    val baseStats: LiveData<BaseStats> = _baseStats.distinctUntilChanged()
    val luckAndHp: LiveData<Pair<Int, Int>> = Transformations.map(_baseStats) { it.luckAndWounds }

    fun onHealClicked() {
        _baseStats.value?.apply {
            if (wounds < vit) wounds++
            else luck++

        }
        _baseStats.notifyChanged()
    }

    fun onDamageClicked() {
        _baseStats.value?.apply {
            if (luck > 0) luck--
            else wounds = maxOf(wounds - 1, 0)

        }
        _baseStats.notifyChanged()
    }

    fun onDamageLongClicked() {
        _baseStats.value?.apply {
            wounds = maxOf(wounds - 1, 0)

        }
        _baseStats.notifyChanged()
    }
}