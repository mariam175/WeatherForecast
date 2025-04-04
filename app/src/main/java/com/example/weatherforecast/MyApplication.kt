package com.example.weatherforecast

import android.app.Application
import android.preference.PreferenceManager
import com.example.weatherforecast.utils.SettingsChanges
import org.osmdroid.config.Configuration
import java.io.File

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
    }
}