package com.example.weatherforecast.data.reopsitry

import com.example.weatherforecast.data.local.IWeatherLocalDataSource
import com.example.weatherforecast.data.local.WeatherLocalDataSource
import com.example.weatherforecast.data.model.Alert
import com.example.weatherforecast.data.model.CityWeather
import com.example.weatherforecast.data.model.CurrentWeather
import com.example.weatherforecast.data.model.DailyAndHourlyWeather
import com.example.weatherforecast.data.model.Favourites
import com.example.weatherforecast.data.model.SearchCity
import com.example.weatherforecast.data.remote.IWeatherRemoteDataSource
import com.example.weatherforecast.data.remote.WeatherRemoteDataSource
import com.example.weatherforecast.utils.SEARCH_URL
import kotlinx.coroutines.flow.Flow

class Repositry(val weatherRemoteDataSource: IWeatherRemoteDataSource, val weatherLocalDataSource: IWeatherLocalDataSource) {
     fun getCurrentWeather(lat:Double , lon:Double , lan:String = "en" , unit:String = "metric"):Flow<CurrentWeather>{
        return weatherRemoteDataSource.getCurrentWeather(lat , lon , lan , unit)
    }
    fun getDailyWeather(lat:Double , lon:Double,lan:String = "en", unit:String = "metric"):Flow<DailyAndHourlyWeather>{
        return weatherRemoteDataSource.getDailyWeather(lat , lon , lan , unit)
    }
    fun getAllFavCities():Flow<List<Favourites>>{
        return weatherLocalDataSource.getAllFavCites()
    }
    suspend fun addCity(city:Favourites):Long{
        return weatherLocalDataSource.addCity(city)
    }
    suspend fun deleteCity(city:Favourites):Int{
        return weatherLocalDataSource.deleteCity(city)
    }
    fun getAllAlert():Flow<List<Alert>>{
        return weatherLocalDataSource.getAllAlerts()
    }
    suspend fun addAlert(alert: Alert):Long{
        return weatherLocalDataSource.addAlert(alert)
    }
    suspend fun deleteAlert(alert: Alert):Int{
        return weatherLocalDataSource.deleteAlert(alert)
    }
     fun getAlertById(id:Long):Flow<Alert>{
        return weatherLocalDataSource.getAlertById(id)
    }
     suspend fun saveCurrentWeather(currentWeather: CurrentWeather): Long {
        return weatherLocalDataSource.saveCurrentWeather(currentWeather)
    }

     fun getCurrentWeather(): Flow<CurrentWeather> {
        return weatherLocalDataSource.getCurrentWeather()
    }
     fun getDailyAndHourly():Flow<DailyAndHourlyWeather>{
        return weatherLocalDataSource.getDailyAndHourly()
    }
     suspend fun saveDailyAndHourly(dailyAndHourlyWeatherEntity: DailyAndHourlyWeather):Long{
        return weatherLocalDataSource.saveDailyAndHourly(dailyAndHourlyWeatherEntity)
    }
     fun getCityWeather(city:String):Flow<CityWeather>{
        return weatherLocalDataSource.getCityWeather(city)
    }
     suspend fun saveCityWeather(cityWeather: CityWeather){
        return weatherLocalDataSource.saveCityWeather(cityWeather)
    }
    suspend fun deleteCityWeather(cityWeather: CityWeather):Int{
        return weatherLocalDataSource.deleteCityWeather(cityWeather)
    }
    suspend fun searchCity(url :String = SEARCH_URL , query:String):Flow<List<SearchCity>>{
         return weatherRemoteDataSource.searchCity(url , query)
    }
}