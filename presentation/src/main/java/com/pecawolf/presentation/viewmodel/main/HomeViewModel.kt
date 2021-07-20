package com.pecawolf.presentation.viewmodel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.distinctUntilChanged
import com.pecawolf.model.BaseStats
import com.pecawolf.presentation.viewmodel.BaseViewModel

class HomeViewModel(
    private val mainViewModel: MainViewModel
) : BaseViewModel() {

    private val _baseStats = mainViewModel.character
    val baseStats: LiveData<BaseStats> = _baseStats.distinctUntilChanged()
    val luckAndHp: LiveData<Pair<Int, Int>> = Transformations.map(_baseStats) { it.luckAndWounds }

    override fun onRefresh() {
    }

    fun onHealClicked() {
        _baseStats.value?.apply {
            if (wounds < vit) wounds++
            else luck++

//            updateCharacter.execute(this)
        }
    }

    fun onDamageClicked() {
        _baseStats.value?.apply {
            if (luck > 0) luck--
            else wounds = maxOf(wounds - 1, 0)

//            updateCharacter.execute(this)
        }
    }

    fun onDamageLongClicked() {
        _baseStats.value?.apply {
            wounds = maxOf(wounds - 1, 0)

//            updateCharacter.execute(this)
        }
    }
}