package com.example.weatherforecast

import android.app.Application
import com.example.weatherforecast.utils.LangaugeChange

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val languageCode = LangaugeChange.getLanguageCode(this)
        LangaugeChange.applyLanguage(this, languageCode)
    }
}