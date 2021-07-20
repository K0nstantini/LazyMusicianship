package com.grommade.lazymusicianship

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class LazyMusicianshipApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // FIXME
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}