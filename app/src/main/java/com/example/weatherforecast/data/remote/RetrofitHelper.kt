package com.example.weatherforecast.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    val url = "https://api.openweathermap.org/data/2.5/"
    val retrofitHelp = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val weatherServices = retrofitHelp.create(WeatherServices::class.java)
}