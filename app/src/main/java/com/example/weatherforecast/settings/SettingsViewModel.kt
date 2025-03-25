package com.example.weatherforecast.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.weatherforecast.utils.SettingsChanges

class SettingsViewModel() : ViewModel() {
    fun changeLanguage(context: Context , code:String){
        SettingsChanges.changeLanguage(context , code)
    }
    fun getCurrentLanguage(context: Context):String{
        return SettingsChanges.getLanguageCode(context)
    }
    fun changeUnit(context: Context , unit:String){
        SettingsChanges.changeUnit(context , unit)
    }
    fun geUnit(context: Context):String{
        return SettingsChanges.getUnit(context)
    }
    fun changeWindSpeed(context: Context , unit:String){
        SettingsChanges.changeWindSpeed(context , unit)
    }
    fun geWindSpeed(context: Context):String{
        return SettingsChanges.getWindSpeed(context)
    }
}