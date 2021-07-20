package com.pecawolf.charactersheet

import android.app.Application
import com.pecawolf.charactersheet.common.CommonModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        startKoin {
            androidContext(this@App)
        }

        UiModule.start()
        CommonModule.start()
    }
}