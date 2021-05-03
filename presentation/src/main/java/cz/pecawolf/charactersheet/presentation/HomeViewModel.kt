package cz.pecawolf.charactersheet.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import cz.pecawolf.charactersheet.common.model.BaseStats
import cz.pecawolf.charactersheet.domain.GetBaseStatsInteractor
import cz.pecawolf.charactersheet.domain.SetBaseStatsInteractor
import cz.pecawolf.charactersheet.presentation.extensions.SingleLiveEvent
import cz.pecawolf.charactersheet.presentation.extensions.notifyChanged
import timber.log.Timber
import kotlin.random.Random

class HomeViewModel(
    private val mainViewModel: MainViewModel,
    val getBaseStats: GetBaseStatsInteractor,
    val setBaseStats: SetBaseStatsInteractor
) : BaseViewModel() {

    private val _baseStats = MutableLiveData<BaseStats>()
    private val _rollResult = SingleLiveEvent<Pair<Int, Boolean>>()

    val baseStats: LiveData<BaseStats> = _baseStats
    val luckAndHp: LiveData<String> = Transformations.map(_baseStats) { it.luckAndWounds }
    val rollResult: LiveData<Pair<Int, Boolean>> = _rollResult

    init {
        loadData()
    }

    fun onHealClicked() {
        _baseStats.value?.apply {
            if (wounds < vit.value) wounds++
            else luck++
            Timber.d("onHealClicked(): $luck + $wounds")
        }
        _baseStats.notifyChanged()

        saveChanges()
    }

    fun onDamageClicked() {
        _baseStats.value?.apply {
            if (luck > 0) luck--
            else wounds = maxOf(wounds - 1, 0)
            Timber.d("onDamageClicked(): $luck + $wounds")
        }
        _baseStats.notifyChanged()

        saveChanges()
    }

    fun onDamageLongClicked() {
        _baseStats.value?.apply {
            wounds = maxOf(wounds - 1, 0)
            Timber.d("onDamageLongClicked(): $luck + $wounds")
        }
        _baseStats.notifyChanged()

        saveChanges()
    }

    fun onStrRollClicked() {
        _baseStats.value?.str?.trap?.let { trap ->
            roll().let { roll ->
                _rollResult.postValue(roll to (roll <= trap))
            }
        }
    }

    fun onDexRollClicked() {
        _baseStats.value?.dex?.trap?.let { trap ->
            roll().let { roll ->
                _rollResult.postValue(roll to (roll <= trap))
            }
        }
    }

    fun onVitRollClicked() {
        _baseStats.value?.vit?.trap?.let { trap ->
            roll().let { roll ->
                _rollResult.postValue(roll to (roll <= trap))
            }
        }
    }

    fun onInlRollClicked() {
        _baseStats.value?.inl?.trap?.let { trap ->
            roll().let { roll ->
                _rollResult.postValue(roll to (roll <= trap))
            }
        }
    }

    fun onWisRollClicked() {
        _baseStats.value?.wis?.trap?.let { trap ->
            roll().let { roll ->
                _rollResult.postValue(roll to (roll <= trap))
            }
        }
    }

    fun onChaRollClicked() {
        _baseStats.value?.cha?.trap?.let { trap ->
            val roll = roll()
            _rollResult.postValue(roll to (roll <= trap))
        }
    }

    private fun roll() = Random(System.currentTimeMillis()).nextInt(20) + 1

    private fun saveChanges() {
        setBaseStats.setStats(_baseStats.value!!)
    }

    private fun loadData() {
        getBaseStats.getStats("VPTNEoSmGeDCBXNhlCNW")
            .observe(
                {
                    Timber.d("onLoadData(): ${it.name}")
                    _baseStats.value = it
                },
                { Timber.e(it, "onLoadDataError(): ") }
            )
    }
}