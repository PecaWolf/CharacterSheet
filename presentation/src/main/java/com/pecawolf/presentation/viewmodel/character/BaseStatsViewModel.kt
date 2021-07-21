package com.pecawolf.presentation.viewmodel.character

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pecawolf.domain.interactor.CreateChracterInteractor
import com.pecawolf.model.BaseStats
import com.pecawolf.model.Constants
import com.pecawolf.presentation.extensions.MergedLiveData3
import com.pecawolf.presentation.extensions.MergedLiveData6
import com.pecawolf.presentation.extensions.SingleLiveEvent
import com.pecawolf.presentation.extensions.dec
import com.pecawolf.presentation.extensions.inc
import com.pecawolf.presentation.viewmodel.BaseViewModel
import timber.log.Timber

class BaseStatsViewModel(
    private val world: BaseStats.World,
    private val species: BaseStats.Species,
    private val createCharacter: CreateChracterInteractor
) : BaseViewModel() {

    private val _name = MutableLiveData<String>("")
    private val _strength = MutableLiveData<Int>(5)
    private val _dexterity = MutableLiveData<Int>(5)
    private val _vitality = MutableLiveData<Int>(5)
    private val _intelligence = MutableLiveData<Int>(5)
    private val _wisdom = MutableLiveData<Int>(5)
    private val _charisma = MutableLiveData<Int>(5)
    private val _navigateToNext = SingleLiveEvent<Unit>()

    val name: LiveData<String> = _name
    val strength: LiveData<Int> = _strength
    val dexterity: LiveData<Int> = _dexterity
    val vitality: LiveData<Int> = _vitality
    val intelligence: LiveData<Int> = _intelligence
    val wisdom: LiveData<Int> = _wisdom
    val charisma: LiveData<Int> = _charisma
    val remaining: LiveData<Int> = MergedLiveData6(
        _strength, _dexterity, _vitality, _intelligence, _wisdom, _charisma
    ) { str, dex, vit, int, wis, cha ->
        Constants.NEW_CHARACTER_STATS_SUM - (str + dex + vit + int + wis + cha)
    }
    val isContinueEnabled: LiveData<Boolean> =
        MergedLiveData3(_name, remaining, isLoading) { name, remaining, isLoading ->
            name.isNotBlank() && remaining == 0 && !isLoading
        }
    val navigateToNext: LiveData<Unit> = _navigateToNext

    fun onNameChanged(name: String) {
        _name.value = name
    }

    fun onStrengthButtonClicked(add: Boolean) {
        if (add) _strength.inc() else _strength.dec()
    }

    fun onDexterityButtonClicked(add: Boolean) {
        if (add) _dexterity.inc() else _dexterity.dec()
    }

    fun onVitalityButtonClicked(add: Boolean) {
        if (add) _vitality.inc() else _vitality.dec()
    }

    fun onIntelligenceButtonClicked(add: Boolean) {
        if (add) _intelligence.inc() else _intelligence.dec()
    }

    fun onWisdomButtonClicked(add: Boolean) {
        if (add) _wisdom.inc() else _wisdom.dec()
    }

    fun onCharismaButtonClicked(add: Boolean) {
        if (add) _charisma.inc() else _charisma.dec()
    }

    fun onContinueClicked() {
        createCharacter.execute(
            BaseStats(
                _name.value!!,
                species,
                world,
                10,
                _vitality.value!!,
                BaseStats.Stat.Strength(_strength.value!!),
                BaseStats.Stat.Dexterity(_dexterity.value!!),
                BaseStats.Stat.Vitality(_vitality.value!!),
                BaseStats.Stat.Intelligence(_intelligence.value!!),
                BaseStats.Stat.Wisdom(_wisdom.value!!),
                BaseStats.Stat.Charisma(_charisma.value!!),
            )
        ).observe(CREATE_CHARACTER, ::onCreateCharacterError, ::onCreateCharacterSuccess)
    }

    private fun onCreateCharacterSuccess() {
        Timber.v("onCreateCharacterSuccess()")

        _navigateToNext.postValue(Unit)
    }

    private fun onCreateCharacterError(error: Throwable) {
        Timber.e(error, "onCreateCharacterError(): ")
    }

    companion object {
        private const val CREATE_CHARACTER = "CREATE_CHARACTER"
    }
}