package com.pecawolf.charactersheet

import android.content.Context
import com.pecawolf.charactersheet.ui.DialogHelper
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object UiModule {
    val instance = module {
        single { (context: Context) -> DialogHelper(context) }
    }

    fun start() {
        loadKoinModules(instance)
    }
}
