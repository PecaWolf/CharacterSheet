package cz.pecawolf.charactersheet

import android.app.Application
import com.google.firebase.FirebaseOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initializeKoin()
        initializeFirebase()
    }


    private fun initializeKoin() {
        startKoin {
            androidContext(this@App)
            module { }
        }

        UiModule.start()
//        RxKoinModule.start()
    }

    private fun initializeFirebase() {
        val options = FirebaseOptions.Builder()
            .setProjectId("staqquest")
            .setApplicationId("1:25949367782:android:4127252ce2c5401ff67a27\n")
            .setApiKey("AIzaSyACUrTZS7LiaVE0dm5j-zkyN0vpklUUGws\n")
            .build()

        Firebase.initialize(this, options, "yourfirebaseprojectname")
    }
}