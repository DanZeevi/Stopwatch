package com.zdan.stopwatch

import android.app.Application
import io.realm.Realm
import timber.log.Timber.DebugTree
import timber.log.Timber.plant

class StopwatchApp: Application() {
    override fun onCreate() {
        super.onCreate()
        // Timber fot logging
        plant(DebugTree())
        // init Realm
        Realm.init(applicationContext)
    }
}