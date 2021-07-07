package com.pecawolf.domain

import com.pecawolf.domain.model.SportResult
import io.reactivex.rxjava3.core.Single

class GetData : BaseInteractor<Nothing?, List<SportResult>>() {
    override fun execute(params: Nothing?): Single<List<SportResult>> {
        return Single.just(listOf())
    }

}