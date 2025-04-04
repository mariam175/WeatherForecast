package com.example.weatherforecast.data.model

import com.google.gson.annotations.SerializedName

data class SearchCity (
    @SerializedName("display_name")
    val cityName:String,
    val lat:Double,
    val lon:Double
)