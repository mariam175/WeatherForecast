package com.example.weatherforecast.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import com.example.weatherforecast.R
import com.google.android.gms.maps.model.LatLng
import java.util.Locale


object SettingsChanges {

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
    fun getIsNotificationEnable(context: Context): Boolean {
        val preferences: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return preferences.getBoolean(ENABLENOTIFY, true) ?: true
    }
    fun isNotificationEnable(context: Context , isEnable:Boolean){
        val preferences: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        preferences.edit().putBoolean(ENABLENOTIFY, isEnable).apply()
    }
    fun getWindSpeed(context: Context): String {
        val preferences: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return preferences.getString(SPEED, context.getString(R.string.unit_m_s)) ?: context.getString(R.string.unit_m_s)
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
    fun saveCurrentLocation(context: Context , lat:Double , lng:Double){
        val preferences: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        preferences.edit().putFloat(CURRLAT, lat.toFloat()).apply()
        preferences.edit().putFloat(CURRLNG, lng.toFloat()).apply()
    }
    fun getCurrentLocation(context: Context):Pair<Double , Double>{
        val preferences: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val lat = preferences.getFloat(CURRLAT, 0f)
        val lng = preferences.getFloat(CURRLNG, 0f)
        return Pair(lat.toDouble() , lng.toDouble())
    }
    fun saveCurrentCity(context: Context , city:String){
        val preferences: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        preferences.edit().putString(CITY, city).apply()
    }
    fun getCurrentCity(context: Context):String{
        val preferences: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val city = preferences.getString(CITY, "")?:""
        return city
    }
}
