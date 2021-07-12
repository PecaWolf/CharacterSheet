package com.pecawolf.domain.interactor

import io.reactivex.rxjava3.core.Observable

abstract class ObservableInteractor<IN, OUT> {
    abstract fun execute(params: IN): Observable<OUT>
}