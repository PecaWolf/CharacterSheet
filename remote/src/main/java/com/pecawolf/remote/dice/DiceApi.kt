package com.pecawolf.remote.dice

import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface DiceApi {
    @GET("integers/")
    fun getNewNumbers(
        @Query("num") num: Int = 25,
        @Query("min") min: Int = 1,
        @Query("max") max: Int = 20,
        @Query("col") col: Int = 25,
        @Query("base") base: Int = 10,
        @Query("format") format: String = "plain",
        @Query("rnd") rnd: String = "new",
    ): Single<ResponseBody>
}
