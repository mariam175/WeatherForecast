package com.example.weatherforecast.data.reopsitry

import com.example.weatherforecast.data.model.CurrentWeather
import com.example.weatherforecast.data.remote.WeatherRemoteDataSource

class Repositry(val weatherRemoteDataSource: WeatherRemoteDataSource) {
    suspend fun getCurrentWeather(lat:Double , lon:Double):CurrentWeather?{
        return weatherRemoteDataSource.getCurrentWeather(lat , lon)
    }
}