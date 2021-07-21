package com.pecawolf.presentation.viewmodel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.distinctUntilChanged
import com.pecawolf.charactersheet.common.extensions.let
import com.pecawolf.domain.interactor.RollDiceInteractor
import com.pecawolf.model.BaseStats
import com.pecawolf.model.RollResult
import com.pecawolf.presentation.extensions.SingleLiveEvent
import com.pecawolf.presentation.viewmodel.BaseViewModel
import timber.log.Timber

class HomeViewModel(
    private val mainViewModel: MainViewModel,
    private val roll: RollDiceInteractor
) : BaseViewModel() {

    private val _baseStats = mainViewModel.character
    private val _navigateTo = SingleLiveEvent<Destination>()
    val baseStats: LiveData<BaseStats> = _baseStats.distinctUntilChanged()
    val luckAndHp: LiveData<Pair<Int, Int>> = Transformations.map(_baseStats) { it.luckAndWounds }
    val navigateTo: LiveData<Destination> = _navigateTo

    fun onStrRollClicked() {
        _baseStats.value?.also {
            _navigateTo.postValue(Destination.RollModifierDialog(it.str))
        }
    }

    fun onDexRollClicked() {
        _baseStats.value?.also {
            _navigateTo.postValue(Destination.RollModifierDialog(it.dex))
        }
    }

    fun onVitRollClicked() {
        _baseStats.value?.also {
            _navigateTo.postValue(Destination.RollModifierDialog(it.vit))
        }
    }

    fun onIntRollClicked() {
        _baseStats.value?.also {
            _navigateTo.postValue(Destination.RollModifierDialog(it.inl))
        }
    }

    fun onWisRollClicked() {
        _baseStats.value?.also {
            _navigateTo.postValue(Destination.RollModifierDialog(it.wis))
        }
    }

    fun onChaRollClicked() {
        _baseStats.value?.also {
            _navigateTo.postValue(Destination.RollModifierDialog(it.cha))
        }
    }

    fun onHealClicked() {
        _baseStats.value?.apply {
            if (wounds < vit.value) wounds++
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

    fun onRollConfirmed(stat: BaseStats.Stat, modifier: String) {
        roll.execute(stat to modifier.toInt())
            .observe(
                ROLL + stat::class.java.simpleName.toUpperCase(),
                ::onRollError,
                ::onRollSuccess
            )
    }

    private fun onRollSuccess(result: Pair<Int, RollResult>) {
        Timber.v("onRollSuccess(): $result")
        _navigateTo.postValue(
            result.let { roll, rollResult -> Destination.RollResultDialog(roll, rollResult) }
        )
    }

    private fun onRollError(error: Throwable) {
        Timber.e(error, "onRollError(): ")
    }

    sealed class Destination {
        data class RollModifierDialog(val stat: BaseStats.Stat) : Destination()
        data class RollResultDialog(val roll: Int, val rollResult: RollResult) : Destination()
    }

    companion object {
        const val ROLL = "ROLL_"
    }
}