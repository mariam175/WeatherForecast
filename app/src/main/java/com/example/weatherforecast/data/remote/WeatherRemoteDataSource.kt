package com.example.weatherforecast.data.remote

import com.example.weatherforecast.data.model.CurrentWeather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRemoteDataSource (val services: WeatherServices){
     fun getCurrentWeather(lat:Double , lon:Double , lan:String = "en" , unit:String = "metric") = flow{
         services.getCurrentWeather(lat , lon , lan , unit).body()?.let { emit(it) }
    }
    fun getDailyWeather(lat:Double , lon:Double , lan:String = "en", unit:String = "metric")  = flow{
        services.getDailyWeather(lat , lon , lan , unit).body()?.let { emit(it) }
    }
}