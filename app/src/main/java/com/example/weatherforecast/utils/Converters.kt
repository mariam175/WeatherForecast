package com.example.weatherforecast.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun convertDate(time: Long): String {
    val date = Date(time * 1000)
    val format = SimpleDateFormat("EEEE d MMMM", Locale.getDefault())
    format.timeZone = TimeZone.getDefault()
    return format.format(date)
}