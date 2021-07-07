package com.pecawolf.domain

import io.reactivex.rxjava3.core.Single

abstract class BaseInteractor<IN, OUT> {
    abstract fun execute(params: IN): Single<OUT>
}