package com.pecawolf.domain.interactor

import com.pecawolf.charactersheet.common.extensions.let
import com.pecawolf.data.DiceRepository
import com.pecawolf.model.BaseStats
import com.pecawolf.model.RollResult
import io.reactivex.rxjava3.core.Single

class RollDiceInteractor(private val repository: DiceRepository) :
    SingleInteractor<Pair<BaseStats.Stat, Int>, Pair<Int, RollResult>>() {

    override fun execute(params: Pair<BaseStats.Stat, Int>): Single<Pair<Int, RollResult>> =
        params.let { stat, modifier ->
            repository.roll()
                .map { roll ->
                    val trap = stat.trap + modifier
                    Pair(
                        roll,
                        if (roll == 1) {
                            RollResult.CriticalSuccess
                        } else if (roll == 20) {
                            RollResult.CriticalFailure
                        } else if (roll <= trap) {
                            RollResult.Success(roll)
                        } else {
                            RollResult.Failure(roll)
                        }
                    )
                }
        }
}
