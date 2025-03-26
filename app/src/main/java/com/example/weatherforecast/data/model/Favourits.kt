package com.example.weatherforecast.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("favouriteCities")
data class Favourites (
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val city:String,
    val lat:Double,
    val lon:Double
)