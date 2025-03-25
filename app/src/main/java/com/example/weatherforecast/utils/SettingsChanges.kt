package com.example.weatherforecast.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import java.util.Locale

object SettingsChanges {
    private const val PREFS_NAME = "settings"
    private const val LANGUAGE_KEY = "language"
    private const val UNIT = "unit"
    private const val SPEED = "speed"
    fun changeLanguage(context: Context, languageCode: String) {
        val preferences: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        preferences.edit().putString(LANGUAGE_KEY, languageCode).apply()

        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    fun getLanguageCode(context: Context): String {
        val preferences: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return preferences.getString(LANGUAGE_KEY, "en") ?: "en"
    }
    fun applyLanguage(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
    fun changeUnit(context: Context , unit:String){
        val preferences: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        preferences.edit().putString(UNIT, unit).apply()
    }
    fun getUnit(context: Context): String {
        val preferences: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return preferences.getString(UNIT, "metric") ?: "metric"
    }
    fun changeWindSpeed(context: Context , unit:String){
        val preferences: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        preferences.edit().putString(SPEED, unit).apply()
    }
    fun getWindSpeed(context: Context): String {
        val preferences: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return preferences.getString(SPEED, "m/s") ?: "m/s"
    }
}
