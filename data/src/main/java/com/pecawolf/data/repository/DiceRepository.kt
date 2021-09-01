package com.pecawolf.data.repository

import com.pecawolf.data.datasource.IDiceRemote
import com.pecawolf.domain.repository.IDiceRepository
import io.reactivex.rxjava3.core.Single
import timber.log.Timber
import java.security.SecureRandom

class DiceRepository(private val remote: IDiceRemote) : IDiceRepository {

    private val random: SecureRandom by lazy { SecureRandom() }
    private val preLoadedNumbers: MutableList<Int> = mutableListOf()

    override fun roll() = if (preLoadedNumbers.size > 5) Single.just(getRandomNumber())
    else remote.getRandom()
        .map {
            preLoadedNumbers.addAll(it)
            getRandomNumber()
        }
        .doOnError { Timber.w(it, "swallowing error: ") }
        .onErrorReturn { if (preLoadedNumbers.size > 5) getRandomNumber() else generateRandomNumber() }

    private fun generateRandomNumber() = random.nextInt(20) + 1

    private fun getRandomNumber() = preLoadedNumbers.removeAt(random.nextInt(preLoadedNumbers.size))
}
