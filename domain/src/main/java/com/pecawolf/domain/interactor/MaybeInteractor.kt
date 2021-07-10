package com.pecawolf.domain.interactor

import io.reactivex.rxjava3.core.Maybe

abstract class MaybeInteractor<IN, OUT> {
    abstract fun execute(params: IN): Maybe<OUT>
}