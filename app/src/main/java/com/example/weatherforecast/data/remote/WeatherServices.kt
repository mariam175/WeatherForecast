package com.example.weatherforecast.data.remote


import com.example.weatherforecast.data.model.CurrentWeather
import com.example.weatherforecast.data.model.DailyAndHourlyWeather
import com.example.weatherforecast.utils.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherServices {
    @GET("weather?appid=$API_KEY&units=metric")
    suspend fun getCurrentWeather(
        @Query("lat") lat:Double ,
        @Query("lon") lon:Double,
        ):Response<CurrentWeather>

    @GET("forecast?appid=$API_KEY&units=metric")
    suspend fun getDailyWeather(
        @Query("lat") lat:Double ,
        @Query("lon") lon:Double,
    ):Response<DailyAndHourlyWeather>

}