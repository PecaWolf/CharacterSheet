package com.pecawolf.presentation.viewmodel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import com.pecawolf.common.extensions.let
import com.pecawolf.domain.interactor.RollDiceInteractor
import com.pecawolf.model.RollResult
import com.pecawolf.model.Rollable.Skill
import com.pecawolf.presentation.extensions.MergedLiveData3
import com.pecawolf.presentation.extensions.SingleLiveEvent
import com.pecawolf.presentation.viewmodel.BaseViewModel
import timber.log.Timber

class SkillsViewModel(
    private val mainViewModel: MainViewModel,
    private val roll: RollDiceInteractor,
    private val updateSkill: com.pecawolf.domain.interactor.UpdateSkillInteractor,
) : BaseViewModel() {

    private val _navigateTo = SingleLiveEvent<Destination>()
    private val _isEditing = MutableLiveData<Boolean>(false)
    private val _isShowingFilters = MutableLiveData<Boolean>(false)
    private val _isShowingUnknown = MutableLiveData<Boolean>(true)
    private val _search = MutableLiveData<String>("")
    val items: LiveData<List<List<Skill>>> = MergedLiveData3(
        mainViewModel.skills,
        _isShowingUnknown,
        _search
    ) { skills, isShowingUnknown, search ->
        Timber.d("onSkills(): ${isShowingUnknown}, $search")
        listOf(
            skills.strength.filter { skill ->
                if (search.isNotBlank()) skill.name.contains(search, true)
                else isShowingUnknown || (skill.value > 0)
            },
            skills.dexterity.filter { skill ->
                if (search.isNotBlank()) skill.name.contains(search, true)
                else isShowingUnknown || (skill.value > 0)
            },
            skills.vitality.filter { skill ->
                if (search.isNotBlank()) skill.name.contains(search, true)
                else isShowingUnknown || (skill.value > 0)
            },
            skills.intelligence.filter { skill ->
                if (search.isNotBlank()) skill.name.contains(search, true)
                else isShowingUnknown || (skill.value > 0)
            },
            skills.wisdom.filter { skill ->
                if (search.isNotBlank()) skill.name.contains(search, true)
                else isShowingUnknown || (skill.value > 0)
            },
            skills.charisma.filter { skill ->
                if (search.isNotBlank()) skill.name.contains(search, true)
                else isShowingUnknown || (skill.value > 0)
            }
        )
    }

    val navigateTo: LiveData<Destination> = _navigateTo
    val isShowingFilters: LiveData<Boolean> = _isShowingFilters
    val isEditing: LiveData<Boolean> = _isEditing
    val isShowingUnknown: LiveData<Boolean> = _isShowingUnknown
    val search: LiveData<String> = _search.distinctUntilChanged()

    fun onSkillsFiltersClicked(isChecked: Boolean) {
        _isShowingFilters.value = isChecked
    }

    fun onSkillsEditClicked(isChecked: Boolean) {
        _isEditing.value = isChecked
    }

    fun onSkillsShowUnknownClicked(isChecked: Boolean) {
        _isShowingUnknown.value = isChecked
    }

    fun onSearchChanged(search: String) {
        _search.value = search
    }

    fun onSearchCancelClicked() {
        _search.value = ""
    }

    fun onRollClicked(skill: Skill) {
        _navigateTo.postValue(Destination.RollModifierDialog(skill))
    }

    fun onEditClicked(skill: Skill) {
        _navigateTo.postValue(Destination.SkillEditDialog(skill))
    }

    fun onRollConfirmed(skill: Skill, modifier: String) {
        roll.execute(skill to modifier.toInt())
            .observe(ROLL + skill.code, ::onRollError, ::onRollSuccess)
    }

    fun onEditConnfirmed(skill: Skill) {
        updateSkill.execute(skill)
            .observe(UPDATE, ::onUpdateSkillsError, ::onUpdateSkillsSuccess)
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

    private fun onUpdateSkillsSuccess() {
        Timber.v("onUpdateSkillsSuccess()")
    }

    private fun onUpdateSkillsError(error: Throwable) {
        Timber.e(error, "onUpdateSkillsError(): ")
    }

    sealed class Destination {
        data class RollModifierDialog(val skill: Skill) : Destination()
        data class RollResultDialog(val roll: Int, val rollResult: RollResult) : Destination()
        data class SkillEditDialog(val skill: Skill) : Destination()
    }

    companion object {
        private const val ROLL = "ROLL_"
        private const val UPDATE = "UPDATE"
    }
}