package com.example.weatherforecast.data.reopsitry

import com.example.weatherforecast.data.model.CurrentWeather
import com.example.weatherforecast.data.remote.WeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow

class Repositry(val weatherRemoteDataSource: WeatherRemoteDataSource) {
     fun getCurrentWeather(lat:Double , lon:Double):Flow<CurrentWeather>{
        return weatherRemoteDataSource.getCurrentWeather(lat , lon)
    }
}