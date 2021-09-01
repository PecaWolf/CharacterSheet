package com.pecawolf.remote.dice

import com.pecawolf.data.datasource.IDiceRemote
import com.pecawolf.remote.Connectivity
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

class DiceRemote(
    private val api: DiceApi,
    private val connectivity: Connectivity,
) : IDiceRemote {

    override fun getRandom(): Single<List<Int>> =
        Single.just(connectivity.run { isConnected() && isConnectedFast() && isConnectedWifi() })
            .flatMap { hasViableConnection ->
                if (hasViableConnection) api.getNewNumbers()
                else Single.error(IllegalStateException("Viable connection not found"))
            }
            .map { it.string().split("\\s+".toRegex()) }
            .flatMapObservable { Observable.fromIterable(it) }
            .filter { it.isNotBlank() }
            .map { it.toInt() }
            .toList()
}