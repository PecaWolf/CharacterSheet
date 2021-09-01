package com.pecawolf.domain.repository

import io.reactivex.rxjava3.core.Single

interface IDiceRepository {
    fun roll(): Single<Int>
}