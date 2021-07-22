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

    private val _items = mainViewModel.character.map {
        listOf(
            setOf(
                Skill("BLACKSMITH", "Blacksmithing", it.str, 1),
                Skill("MINING", "Mining", it.str, 1),
            ),
            setOf(
                Skill("FENCING", "Fencing", it.dex, 1),
                Skill("ARCHERY", "Archery", it.dex, 1),
            ),
            setOf(
                Skill("ASKETISM", "Asketism", it.vit, 1),
                Skill("HARD_LABOR", "Hard labor", it.vit, 1),
            ),
            setOf(
                Skill("RUNESMITH", "Runesmithing", it.wis, 1),
                Skill("Alchemy", "Alchemy", it.wis, 1),
            ),
            setOf(
                Skill("PYROMANCY", "Pyromancy", it.inl, 1),
                Skill("Caelomancy", "Caelomancy", it.inl, 1),
            ),
            setOf(
                Skill("PERSUASION", "Persuasion", it.cha, 1),
                Skill("HAGGLING", "Haggling", it.cha, 1),
            ),
        )
    }
    private val _navigateTo = SingleLiveEvent<Destination>()
    val items: LiveData<List<Set<Skill>>> = _items
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