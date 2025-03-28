package com.example.weatherforecast.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("alert")
data class Alert (
    @PrimaryKey(autoGenerate = true)
    var alertId:Long = 0,
    val cityName:String,
    val time:String,
)