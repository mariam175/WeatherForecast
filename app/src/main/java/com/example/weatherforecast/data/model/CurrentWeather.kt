package com.example.weatherforecast.data.model

data class CurrentWeather(
    val id:Int,
    val name:String,
    val main:Temp,
    val weather:List<Weather>,
    val dt:Long,
    val sys: Sys,
    val wind: Wind,
    val clouds: Clouds
)
data class Temp(
    val temp:Double,
    val pressure:Int,
    val humidity:Int,
    val feels_like:Double
)
data class Weather(
    val main:String,
    val description:String,
    val icon:String
)
data class Sys(
    val country:String,
)
data class Wind(
    val speed:Double,
    val deg:Int,
    val gust:Double
)
data class Clouds(
    val all:Int
)