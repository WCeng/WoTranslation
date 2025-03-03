package com.wceng.wotranslation

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WoApplication: Application() {

    override fun onCreate() {
        super.onCreate()
    }
}