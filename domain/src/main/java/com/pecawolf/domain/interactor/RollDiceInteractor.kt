package com.pecawolf.domain.interactor

import com.pecawolf.charactersheet.common.extensions.let
import com.pecawolf.data.DiceRepository
import com.pecawolf.model.RollResult
import com.pecawolf.model.Rollable
import io.reactivex.rxjava3.core.Single

class RollDiceInteractor(private val repository: DiceRepository) :
    SingleInteractor<Pair<Rollable, Int>, Pair<Int, RollResult>>() {

    override fun execute(params: Pair<Rollable, Int>): Single<Pair<Int, RollResult>> =
        params.let { stat, modifier ->
            repository.roll()
                .map { roll ->
                    Pair(
                        roll,
                        when {
                            roll == 1 -> RollResult.CriticalSuccess
                            roll == 20 -> RollResult.CriticalFailure
                            roll <= stat.trap + modifier -> RollResult.Success(roll)
                            else -> RollResult.Failure(roll)
                        }
                    )
                }
        }
}
