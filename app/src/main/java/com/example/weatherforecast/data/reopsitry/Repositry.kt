package com.example.weatherforecast.data.reopsitry

import com.example.weatherforecast.data.model.CurrentWeather
import com.example.weatherforecast.data.model.DailyAndHourlyWeather
import com.example.weatherforecast.data.remote.WeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow

class Repositry(val weatherRemoteDataSource: WeatherRemoteDataSource) {
     fun getCurrentWeather(lat:Double , lon:Double , lan:String = "en" , unit:String = "metric"):Flow<CurrentWeather>{
        return weatherRemoteDataSource.getCurrentWeather(lat , lon , lan , unit)
    }
    fun getDailyWeather(lat:Double , lon:Double,lan:String = "en", unit:String = "metric"):Flow<DailyAndHourlyWeather>{
        return weatherRemoteDataSource.getDailyWeather(lat , lon , lan , unit)
    }
}