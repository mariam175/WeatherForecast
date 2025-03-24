package com.example.weatherforecast.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.weatherforecast.utils.LangaugeChange

class SettingsViewModel() : ViewModel() {
    fun changeLanguage(context: Context , code:String){
        LangaugeChange.changeLanguage(context , code)
    }
    fun getCurrentLanguage(context: Context):String{
        return LangaugeChange.getLanguageCode(context)
    }
}