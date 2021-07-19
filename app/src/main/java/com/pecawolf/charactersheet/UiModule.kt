package com.pecawolf.charactersheet

import android.content.Context
import com.pecawolf.charactersheet.ui.DialogHelper
import com.pecawolf.presentation.PresentationModule
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object UiModule {
    val instance = module {
        single { (context: Context) -> DialogHelper(context) }
    }

    fun start() {
        PresentationModule.start()
        loadKoinModules(instance)
    }
}
