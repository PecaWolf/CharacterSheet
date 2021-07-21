package com.pecawolf.presentation.viewmodel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.distinctUntilChanged
import com.pecawolf.charactersheet.common.extensions.let
import com.pecawolf.domain.interactor.RollDiceInteractor
import com.pecawolf.domain.interactor.UpdateCharacterInteractor
import com.pecawolf.model.BaseStats
import com.pecawolf.model.RollResult
import com.pecawolf.presentation.extensions.SingleLiveEvent
import com.pecawolf.presentation.extensions.toggle
import com.pecawolf.presentation.viewmodel.BaseViewModel
import timber.log.Timber
import kotlin.math.max

class HomeViewModel(
    private val mainViewModel: MainViewModel,
    private val roll: RollDiceInteractor,
    private val updateCharacter: UpdateCharacterInteractor,
) : BaseViewModel() {

    private val _isEditing = MutableLiveData<Boolean>(false)
    private val _navigateTo = SingleLiveEvent<Destination>()
    val baseStats: LiveData<BaseStats> = mainViewModel.character.distinctUntilChanged()
    val luckAndHp: LiveData<Pair<Int, Int>> = Transformations.map(baseStats) { it.luckAndWounds }
    val isEditing: LiveData<Boolean> = _isEditing
    val navigateTo: LiveData<Destination> = _navigateTo

    fun onNameEdit() {
        baseStats.value?.also {
            _navigateTo.postValue(Destination.EditNameDialog(it.name))
        }
    }

    fun onNameChanged(name: String) {
        baseStats.value?.also { baseStats ->
            baseStats.name = name

            updateCharacter.execute(baseStats)
                .observe(UPDATE, ::onUpdateCharacterError, ::onUpdateCharacterSuccess)
        }
    }

    fun onRollClicked(stat: BaseStats.Stat) {
        baseStats.value?.also {
            _navigateTo.postValue(Destination.RollModifierDialog(stat))
        }
    }

    fun onHealClicked(heal: Int) {
        baseStats.value?.also { baseStats ->
            if (baseStats.wounds < baseStats.vit.value) {
                baseStats.wounds += heal
                val over = baseStats.wounds - baseStats.vit.value
                baseStats.luck += over
                baseStats.wounds -= max(over, 0)
            } else baseStats.luck += heal

            updateCharacter.execute(baseStats)
                .observe(UPDATE, ::onUpdateCharacterError, ::onUpdateCharacterSuccess)
        }
    }

    fun onDamageClicked(damage: Int) {
        baseStats.value?.also { baseStats ->
            if (baseStats.luck > 0) {
                baseStats.luck -= damage
            } else baseStats.wounds = maxOf(baseStats.wounds - damage, 0)

            updateCharacter.execute(baseStats)
                .observe(UPDATE, ::onUpdateCharacterError, ::onUpdateCharacterSuccess)
        }
    }

    fun onDamageLongClicked() {
        baseStats.value?.also { baseStats ->
            baseStats.wounds = maxOf(baseStats.wounds - 1, 0)

            updateCharacter.execute(baseStats)
                .observe(UPDATE, ::onUpdateCharacterError, ::onUpdateCharacterSuccess)
        }
    }

    fun onEditClicked() {
        _isEditing.toggle()
    }

    private fun onUpdateCharacterSuccess() {
        Timber.v("onUpdateCharacterSuccess()")
    }

    private fun onUpdateCharacterError(error: Throwable) {
        Timber.e(error, "onUpdateCharacterError(): ")
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
        data class EditNameDialog(val name: String) : Destination()
    }

    companion object {
        const val ROLL = "ROLL_"
        const val UPDATE = "UPDATE"
    }
}