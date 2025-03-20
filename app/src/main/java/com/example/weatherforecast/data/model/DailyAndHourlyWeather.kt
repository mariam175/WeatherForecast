package com.example.weatherforecast.data.model

data class DailyAndHourlyWeather (
    val list:List<CurrentWeather>
)
data class Daily(
    val id:Int,
    val main:DailyMain,
    val weather:List<DailyWeather>,
    val dt:Long,
    val wind: DailyWind,
    val clouds: DailyClouds,
    val dt_txt:String
)
data class DailyMain(
    val temp:Double,
    val pressure:Int,
    val humidity:Int,
    val feels_like:Double
)
data class DailyWeather(
    val main:String,
    val description:String,
    val icon:String
)
data class DailyWind(
    val speed:Double,
    val deg:Int,
    val gust:Double
)
data class DailyClouds(
    val all:Int
)