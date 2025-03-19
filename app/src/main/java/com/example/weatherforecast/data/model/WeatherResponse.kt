package com.example.weatherforecast.data.model

sealed class WeatherResponse {
    data object Loading:WeatherResponse()
    data class Success(val data:CurrentWeather):WeatherResponse()
    data class Failure(val error:Throwable):WeatherResponse()
}