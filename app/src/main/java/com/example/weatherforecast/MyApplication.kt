package com.example.weatherforecast

import android.app.Application
import com.example.weatherforecast.utils.SettingsChanges

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val languageCode = SettingsChanges.getLanguageCode(this)
        SettingsChanges.applyLanguage(this, languageCode)
    }
}