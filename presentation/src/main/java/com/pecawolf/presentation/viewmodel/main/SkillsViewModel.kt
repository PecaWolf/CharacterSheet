package com.pecawolf.presentation.viewmodel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pecawolf.charactersheet.common.extensions.let
import com.pecawolf.domain.interactor.RollDiceInteractor
import com.pecawolf.model.RollResult
import com.pecawolf.model.Rollable.Skill
import com.pecawolf.presentation.extensions.MergedLiveData2
import com.pecawolf.presentation.extensions.SingleLiveEvent
import com.pecawolf.presentation.extensions.toggle
import com.pecawolf.presentation.viewmodel.BaseViewModel
import timber.log.Timber

class SkillsViewModel(
    private val mainViewModel: MainViewModel,
    private val roll: RollDiceInteractor,
) : BaseViewModel() {

    private val _navigateTo = SingleLiveEvent<Destination>()
    private val _isEditing = MutableLiveData<Boolean>(false)
    private val _isShowingUnknown = MutableLiveData<Boolean>(true)
    val items: LiveData<List<List<Skill>>> =
        MergedLiveData2(mainViewModel.skills, _isShowingUnknown) { skills, isShowingUnknown ->
            Timber.w("onSkills(): $isShowingUnknown, ${skills.strength.size}, ${skills.dexterity.size}, ${skills.vitality.size}, ${skills.intelligence.size}, ${skills.wisdom.size}, ${skills.charisma.size}")
            listOf(
                skills.strength.filter { skill ->
                    isShowingUnknown || (skill.value > 0)
                },
                skills.dexterity.filter { skill ->
                    isShowingUnknown || (skill.value > 0)

                },
                skills.vitality.filter { skill ->
                    isShowingUnknown || (skill.value > 0)
                },
                skills.intelligence.filter { skill ->
                    isShowingUnknown || (skill.value > 0)
                },
                skills.wisdom.filter { skill ->
                    isShowingUnknown || (skill.value > 0)
                },
                skills.charisma.filter { skill ->
                    isShowingUnknown || (skill.value > 0)
                },
            )
        }

    val navigateTo: LiveData<Destination> = _navigateTo
    val isEditing: LiveData<Boolean> = _isEditing
    val isShowingUnknown: LiveData<Boolean> = _isShowingUnknown

    fun onSkillsEditClicked() {
        _isEditing.toggle()
    }

    fun onSkillsShowUnknownClicked() {
        _isShowingUnknown.toggle()
    }


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