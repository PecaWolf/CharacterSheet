package com.pecawolf.data

import com.pecawolf.remote.Connectivity
import com.pecawolf.remote.dice.IDiceRemote
import io.reactivex.rxjava3.core.Single
import timber.log.Timber
import java.security.SecureRandom

class DiceRepository(private val remote: IDiceRemote, private val connectivity: Connectivity) {

    private val random: SecureRandom by lazy { SecureRandom() }
    private val preLoadedNumbers: MutableList<Int> = mutableListOf()

    fun roll(): Single<Int> {
        return connectivity.run { isConnected() && isConnectedFast() && isConnectedWifi() }
            .let { hasViableConnection ->
                Timber.d("roll(): $hasViableConnection $preLoadedNumbers")
                when {
                    preLoadedNumbers.size > 5 -> {
                        Single.just(getRandomNumber())
                    }
                    hasViableConnection -> {
                        remote.getRandom()
                            .doOnError { Timber.w(it, "swallowing error: ") }
                            .map {
                                preLoadedNumbers.addAll(it)
                                getRandomNumber()
                            }
                            .onErrorReturn {
                                if (preLoadedNumbers.size > 5) getRandomNumber() else generateRandomNumber()
                            }
                    }
                    else -> {
                        Single.just(generateRandomNumber())
                    }
                }
            }
    }

    private fun generateRandomNumber() = random.nextInt(20) + 1

    private fun getRandomNumber() = preLoadedNumbers.removeAt(random.nextInt(preLoadedNumbers.size))
}
