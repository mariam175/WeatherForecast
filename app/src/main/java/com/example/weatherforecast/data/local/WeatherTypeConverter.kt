package com.example.weatherforecast.data.local

import androidx.room.TypeConverter
import com.example.weatherforecast.data.model.CurrentWeather
import com.example.weatherforecast.data.model.DailyAndHourlyWeather
import com.example.weatherforecast.data.model.Temp
import com.example.weatherforecast.data.model.Weather
import com.example.weatherforecast.data.model.Wind
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
class TempConverter {
    @TypeConverter
    fun fromTemp(temp: Temp): String = Gson().toJson(temp)

    @TypeConverter
    fun toTemp(json: String): Temp = Gson().fromJson(json, object : TypeToken<Temp>() {}.type)
}

class WeatherConverter {
    @TypeConverter
    fun fromWeather(weather: List<Weather>): String = Gson().toJson(weather)

    @TypeConverter
    fun toWeather(json: String): List<Weather> =
        Gson().fromJson(json, object : TypeToken<List<Weather>>() {}.type)
}

class WindConverter {
    @TypeConverter
    fun fromWind(wind: Wind): String = Gson().toJson(wind)

    @TypeConverter
    fun toWind(json: String): Wind = Gson().fromJson(json, object : TypeToken<Wind>() {}.type)
}
class ListCurrentWeather{
    @TypeConverter
    fun fromCurrentWeatherList(list: List<CurrentWeather>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toCurrentWeatherList(data: String): List<CurrentWeather> {
        val type = object : TypeToken<List<CurrentWeather>>() {}.type
        return Gson().fromJson(data, type)
    }
    @TypeConverter
    fun fromCurrentWeather(weather: CurrentWeather): String {
        return Gson().toJson(weather)
    }

    @TypeConverter
    fun toCurrentWeather(json: String): CurrentWeather {
        val type = object : TypeToken<CurrentWeather>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun fromDailyWeather(dailyWeather: DailyAndHourlyWeather): String {
        return Gson().toJson(dailyWeather)
    }

    @TypeConverter
    fun toDailyWeather(json: String): DailyAndHourlyWeather {
        val type = object : TypeToken<DailyAndHourlyWeather>() {}.type
        return Gson().fromJson(json, type)
    }
}