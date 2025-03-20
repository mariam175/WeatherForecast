package com.example.weatherforecast.data.model

sealed class DailyWeatherResponse {
    data object Loading:DailyWeatherResponse()
    data class Success(val data:DailyAndHourlyWeather):DailyWeatherResponse()
    data class Failure(val error:Throwable):DailyWeatherResponse()
}