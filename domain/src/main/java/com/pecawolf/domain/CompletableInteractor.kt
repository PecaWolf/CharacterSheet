package com.pecawolf.domain

import io.reactivex.rxjava3.core.Completable

abstract class CompletableInteractor<IN> {
    abstract fun execute(params: IN): Completable
}