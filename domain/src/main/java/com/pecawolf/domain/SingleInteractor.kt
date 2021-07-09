package com.pecawolf.domain

import io.reactivex.rxjava3.core.Single

abstract class SingleInteractor<IN, OUT> {
    abstract fun execute(params: IN): Single<OUT>
}