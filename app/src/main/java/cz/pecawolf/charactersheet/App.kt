package cz.pecawolf.charactersheet

import android.app.Application
import org.koin.core.context.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin { modules(presentationModule, domainModule, dataModule) }
    }
}