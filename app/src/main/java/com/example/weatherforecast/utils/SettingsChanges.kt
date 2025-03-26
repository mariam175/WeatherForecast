package com.example.weatherforecast.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import com.google.android.gms.maps.model.LatLng
import java.util.Locale

object SettingsChanges {
    private const val PREFS_NAME = "settings"
    private const val LANGUAGE_KEY = "language"
    private const val UNIT = "unit"
    private const val SPEED = "speed"
    private const val LOCATION = "location"
    private const val LAT = "lat"
    private const val LNG = "lng"
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
    fun changeLocation(context: Context , locType:String){
        val preferences: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        preferences.edit().putString(LOCATION, locType).apply()
    }
    fun getLocationType(context: Context): String {
        val preferences: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return preferences.getString(LOCATION, "gps") ?: "gps"
    }
    fun saveLocation(context: Context , lat:Double , lng:Double ){
        val preferences: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        preferences.edit().putFloat(LAT, lat.toFloat()).apply()
        preferences.edit().putFloat(LNG, lng.toFloat()).apply()
    }
    fun getLatLangType(context: Context):Pair<Double , Double>{
        val preferences: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
         val lat = preferences.getFloat(LAT, 0f)
         val lng = preferences.getFloat(LNG, 0f)
        return Pair(lat.toDouble() , lng.toDouble())
    }
}
