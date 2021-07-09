package com.pecawolf.domain

import io.reactivex.rxjava3.core.Maybe

abstract class MaybeInteractor<IN, OUT> {
    abstract fun execute(params: IN): Maybe<OUT>
}