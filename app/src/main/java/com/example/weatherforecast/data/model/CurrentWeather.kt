package com.example.weatherforecast.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("current")
data class CurrentWeather(
    @PrimaryKey
    val currId:Int = 0,
    val id:Int,
    val name:String,
    val main:Temp,
    val weather:List<Weather>,
    val dt:Long,
    val wind: Wind,

)
data class Temp(
    var temp:Double,
    val temp_min:Double,
    val temp_max:Double,
    val pressure:Int,
    val humidity:Int,
    val feels_like:Double
)
data class Weather(
    val main:String,
    var description:String,
    val icon:String
)

data class Wind(
    val speed:Double,
)
