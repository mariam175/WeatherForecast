package com.example.weatherforecast.utils

import com.example.weatherforecast.R

class WeatherConditions {
    companion object{
        fun getWeatherConditions(icon:String) : Int{
            val icon = when(icon){
                "01d" ->R.drawable.clear_day
                "01n" ->R.drawable.night
                "02d" -> R.drawable.partly_cloudy_day
                "02n" -> R.drawable.partly_cloudy_night
                "03d" ->R.drawable.cloudy
                "03n" ->R.drawable.cloudy
                "04d" ->R.drawable.overcast_day
                "04n" ->R.drawable.overcast_night
                "50n" ->R.drawable.mist
                "50d" ->R.drawable.mist
                "09d"->R.drawable.drizzle
                "09n"->R.drawable.drizzle
                "10d"->R.drawable.partly_cloudy_day_rain
                "10n"->R.drawable.partly_cloudy_night_rain
                "11d"->R.drawable.thundstorm
                "11n"->R.drawable.thundstorm
                "13d" ->R.drawable.snow
                else ->R.drawable.snow
            }
            return icon
        }
    }
}