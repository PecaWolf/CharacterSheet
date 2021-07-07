package com.pecawolf.domain

import com.pecawolf.domain.model.SportResult
import io.reactivex.rxjava3.core.Single

class AddEntry : BaseInteractor<AddEntry.Params, List<SportResult>>() {

    override fun execute(params: Params): Single<List<SportResult>> {
        return Single.just(listOf())
    }

    data class Params(
        val title: String,
        val place: String,
        val duration: Long,
        val upload: Boolean
    )
}