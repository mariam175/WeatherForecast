package com.example.weatherforecast.data.reopsitry

import com.example.weatherforecast.data.local.CitiesLocalDataSource
import com.example.weatherforecast.data.model.CurrentWeather
import com.example.weatherforecast.data.model.DailyAndHourlyWeather
import com.example.weatherforecast.data.model.Favourites
import com.example.weatherforecast.data.remote.WeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow

class Repositry(val weatherRemoteDataSource: WeatherRemoteDataSource , val cityLocalDataSource: CitiesLocalDataSource) {
     fun getCurrentWeather(lat:Double , lon:Double , lan:String = "en" , unit:String = "metric"):Flow<CurrentWeather>{
        return weatherRemoteDataSource.getCurrentWeather(lat , lon , lan , unit)
    }
    fun getDailyWeather(lat:Double , lon:Double,lan:String = "en", unit:String = "metric"):Flow<DailyAndHourlyWeather>{
        return weatherRemoteDataSource.getDailyWeather(lat , lon , lan , unit)
    }
    fun getAllFavCities():Flow<List<Favourites>>{
        return cityLocalDataSource.getAllFavCites()
    }
    suspend fun addCity(city:Favourites):Long{
        return cityLocalDataSource.addCity(city)
    }
    suspend fun deleteCity(city:Favourites):Int{
        return cityLocalDataSource.deleteCity(city)
    }
}