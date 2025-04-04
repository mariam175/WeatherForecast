package com.example.weatherforecast.data.remote


import com.example.weatherforecast.data.model.CurrentWeather
import com.example.weatherforecast.data.model.DailyAndHourlyWeather
import com.example.weatherforecast.data.model.SearchCity
import com.example.weatherforecast.utils.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface WeatherServices {
    @GET("weather?appid=$API_KEY")
    suspend fun getCurrentWeather(
        @Query("lat") lat:Double ,
        @Query("lon") lon:Double,
        @Query("lang") lan:String = "en",
        @Query("units") units:String = "metric"
        ):Response<CurrentWeather>

    @GET("forecast?appid=$API_KEY")
    suspend fun getDailyWeather(
        @Query("lat") lat:Double ,
        @Query("lon") lon:Double,
        @Query("lang") lan:String = "en",
        @Query("units") units:String = "metric"
    ):Response<DailyAndHourlyWeather>

    @GET
    suspend fun searchCity(
        @Url url:String,
        @Query("q") query: String,
        @Query("format") format:String = "json",
        @Query("accept-language") language: String = "en"
    ):Response<List<SearchCity>>

}