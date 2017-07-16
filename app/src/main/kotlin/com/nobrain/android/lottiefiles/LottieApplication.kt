package com.nobrain.android.lottiefiles

import android.app.Application
import com.facebook.stetho.Stetho


class LottieApplication : Application() {
    lateinit var appComponent: LottieApplicationComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerLottieApplicationComponent.builder()
            .lottieApplicationModule(LottieApplicationModule(this))
            .build()
        app = this
        Stetho.initializeWithDefaults(this)

    }

    companion object {
        lateinit var app: LottieApplication
            private set
    }
}

