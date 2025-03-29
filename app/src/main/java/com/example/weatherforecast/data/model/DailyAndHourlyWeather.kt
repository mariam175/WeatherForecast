package com.example.weatherforecast.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("daily_hourly_weather")
data class DailyAndHourlyWeather (
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val list:List<CurrentWeather>
)
