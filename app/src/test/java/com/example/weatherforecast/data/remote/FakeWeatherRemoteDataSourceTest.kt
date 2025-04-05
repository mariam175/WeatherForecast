package com.example.weatherforecast.data.remote


import com.example.weatherforecast.data.model.CurrentWeather
import com.example.weatherforecast.data.model.DailyAndHourlyWeather
import com.example.weatherforecast.data.model.SearchCity

import com.example.weatherforecast.data.model.Temp
import com.example.weatherforecast.data.model.Weather
import com.example.weatherforecast.data.model.Wind
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeWeatherRemoteDataSourceTest :IWeatherRemoteDataSource {
    var currentWeather =  CurrentWeather(
        id = 1,
        name = "New York",
        main = Temp(
            temp = 22.5,
            temp_min = 20.0,
            temp_max = 25.0,
            pressure = 1013,
            humidity = 65,
            feels_like = 23.0
        ),
        weather = listOf(
            Weather(
                main = "Clear",
                description = "Clear sky",
                icon = "01d"
            )
        ),
        dt = 1711823400,

        wind = Wind(
            speed = 5.5,

        ),

    )
    override fun getCurrentWeather(
        lat: Double,
        lon: Double,
        lan: String,
        unit: String
    ): Flow<CurrentWeather> {
            if (lan == "ar")
                currentWeather.weather.get(0).description = "سماء صافية"
            else if (unit == "Imperial") currentWeather.main.temp = 75.5
            else if (unit == "Standard") currentWeather.main.temp = 295.65
        return flow {
            emit(currentWeather)
        }
    }

    override fun getDailyWeather(
        lat: Double,
        lon: Double,
        lan: String,
        unit: String
    ): Flow<DailyAndHourlyWeather> {
        TODO("Not yet implemented")
    }

    override fun searchCity(url: String, query: String): Flow<List<SearchCity>> {
        TODO("Not yet implemented")
    }
}