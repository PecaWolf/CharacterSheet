package com.pecawolf.charactersheet

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.pecawolf.charactersheet.common.CommonModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(
            this,
            FirebaseOptions.Builder()
                .setApplicationId("com.pecawolf.charactersheet")
                .setDatabaseUrl("https://staqquest.firebaseio.com/")
                .build()
        )

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        startKoin {
            androidContext(this@App)
        }

        UiModule.start()
        CommonModule.start()
    }
}