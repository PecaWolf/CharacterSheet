package com.pecawolf.presentation.viewmodel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.pecawolf.charactersheet.common.extensions.let
import com.pecawolf.domain.interactor.RollDiceInteractor
import com.pecawolf.model.RollResult
import com.pecawolf.model.Rollable.Skill
import com.pecawolf.presentation.extensions.SingleLiveEvent
import com.pecawolf.presentation.viewmodel.BaseViewModel
import timber.log.Timber

class SkillsViewModel(
    private val mainViewModel: MainViewModel,
    private val roll: RollDiceInteractor,
) : BaseViewModel() {

    private val _navigateTo = SingleLiveEvent<Destination>()
    val items: LiveData<List<List<Skill>>> = mainViewModel.skills.map {
        Timber.w("onSkills(): $it")
        listOf(
            it.strength,
            it.dexterity,
            it.vitality,
            it.intelligence,
            it.wisdom,
            it.charisma,
        )
    }
    val navigateTo: LiveData<Destination> = _navigateTo


    fun onRollClicked(skill: Skill) {
        _navigateTo.postValue(Destination.RollModifierDialog(skill))
    }

    fun onRollConfirmed(skill: Skill, modifier: String) {
        roll.execute(skill to modifier.toInt())
            .observe(
                ROLL + skill.code,
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
        data class RollModifierDialog(val skill: Skill) : Destination()
        data class RollResultDialog(val roll: Int, val rollResult: RollResult) : Destination()
    }

    companion object {
        private const val ROLL = "ROLL_"
    }
}