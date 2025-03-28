package com.example.weatherforecast.data.remote

import com.example.weatherforecast.data.model.CurrentWeather
import com.example.weatherforecast.data.model.DailyAndHourlyWeather
import kotlinx.coroutines.flow.Flow

interface IWeatherRemoteDataSource {
    fun getCurrentWeather(
        lat: Double,
        lon: Double,
        lan: String = "en",
        unit: String = "metric"
    ): Flow<CurrentWeather>

    fun getDailyWeather(
        lat: Double,
        lon: Double,
        lan: String = "en",
        unit: String = "metric"
    ): Flow<DailyAndHourlyWeather>
}