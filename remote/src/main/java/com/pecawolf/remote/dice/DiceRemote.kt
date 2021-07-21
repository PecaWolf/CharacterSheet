package com.pecawolf.remote.dice

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

class DiceRemote(private val api: DiceApi) : IDiceRemote {
    override fun getRandom(): Single<List<Int>> = api.getNewNumbers()
        .map { it.string().split("\\s+".toRegex()) }
        .flatMapObservable { Observable.fromIterable(it) }
        .filter { it.isNotBlank() }
        .map { it.toInt() }
        .toList()

}
