package com.example.weatherforecast.data.remote

import com.example.weatherforecast.data.model.CurrentWeather
import kotlinx.coroutines.flow.flow

class WeatherRemoteDataSource (val services: WeatherServices){
     fun getCurrentWeather(lat:Double , lon:Double)  = flow{
         services.getCurrentWeather(lat , lon).body()?.let { emit(it) }
    }
}