package com.example.weatherforecast.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("cityWeather")
data class CityWeather (
    @PrimaryKey
    val city:String,
    val currentWeather: CurrentWeather,
    val list:DailyAndHourlyWeather
)