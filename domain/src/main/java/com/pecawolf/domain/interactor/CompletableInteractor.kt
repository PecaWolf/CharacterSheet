package com.pecawolf.domain.interactor

import io.reactivex.rxjava3.core.Completable

abstract class CompletableInteractor<IN> {
    abstract fun execute(params: IN): Completable
}