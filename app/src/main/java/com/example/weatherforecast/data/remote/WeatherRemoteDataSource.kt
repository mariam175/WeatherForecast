package com.example.weatherforecast.data.remote

import com.example.weatherforecast.data.model.CurrentWeather

class WeatherRemoteDataSource (val services: WeatherServices){
    suspend fun getCurrentWeather(lat:Double , lon:Double) : CurrentWeather?{
        return services.getCurrentWeather(lat , lon).body()
    }
}