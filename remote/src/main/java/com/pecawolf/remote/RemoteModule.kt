package com.pecawolf.remote

import com.pecawolf.remote.dice.DiceApi
import com.pecawolf.remote.dice.DiceRemote
import com.pecawolf.remote.dice.IDiceRemote
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object RemoteModule {
    val instance = module {
        single { Connectivity(get()) }
        single { DiceRemote(get()) as IDiceRemote }

        single {
            Moshi.Builder()
                .build() as Moshi
        }
        single {
            OkHttpClient.Builder()
                .addInterceptor { chain ->
                    chain.request().newBuilder()
                        .build()
//                        .also { Timber.d("OkHttp: ${it.method} –> ${it.url}") }
                        .let { chain.proceed(it) }
//                        .also {
//                            if (it.code < 400) Timber.d("OkHttp: ${it.code}: ${it.peekBody(1024).string()}")
//                            else Timber.e("OkHttp: ${it.code}: ${it.peekBody(1024).string()}")
//                        }
                }
                .build()

        }
        single {
            Retrofit.Builder()
                .baseUrl("https://www.random.org/")
                .addConverterFactory(MoshiConverterFactory.create(get()))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(get())
                .build()
                .create(DiceApi::class.java)
        }
    }

    fun start() {
        loadKoinModules(instance)
    }
}