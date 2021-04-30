package cz.pecawolf.charactersheet

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import cz.pecawolf.charactersheet.common.IRemote
import cz.pecawolf.charactersheet.presentation.PresentationModule
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object UiModule {
    val instance = module {
        single { Firebase.firestore }
        single { RemoteImpl(get()) as IRemote }
    }

    fun start() {
        PresentationModule.start()
        loadKoinModules(instance)
    }
}
