package com.pecawolf.injection

import com.pecawolf.cache.CacheModule
import com.pecawolf.common.CommonModule
import com.pecawolf.data.DataModule
import com.pecawolf.domain.DomainModule
import com.pecawolf.presentation.PresentationModule
import com.pecawolf.remote.RemoteModule

object BaseModule {
    fun start() {
        PresentationModule.start()
        DomainModule.start()
        DataModule.start()
        CacheModule.start()
        RemoteModule.start()
        CommonModule.start()
    }
}