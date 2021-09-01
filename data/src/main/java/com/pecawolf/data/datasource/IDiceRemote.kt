package com.pecawolf.data.datasource

import io.reactivex.rxjava3.core.Single

interface IDiceRemote {
    fun getRandom(): Single<List<Int>>
}
