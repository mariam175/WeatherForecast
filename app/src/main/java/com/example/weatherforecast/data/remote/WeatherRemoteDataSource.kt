package com.example.weatherforecast.data.remote

import com.example.weatherforecast.data.model.CurrentWeather
import com.example.weatherforecast.data.model.SearchCity
import com.example.weatherforecast.utils.SEARCH_URL
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRemoteDataSource (val services: WeatherServices) : IWeatherRemoteDataSource {
     override fun getCurrentWeather(lat:Double, lon:Double, lan:String, unit:String) = flow{
         services.getCurrentWeather(lat , lon , lan , unit).body()?.let { emit(it) }
    }
    override fun getDailyWeather(lat:Double, lon:Double, lan:String, unit:String)  = flow{
        services.getDailyWeather(lat , lon , lan , unit).body()?.let { emit(it) }
    }

    override fun searchCity(url: String, query: String) = flow {
        services.searchCity(url , query).body()?.let { emit(it) }
    }
}