package com.example.weatherforecast.data.local

import com.example.weatherforecast.data.model.Alert

sealed class AlertState {
    data object Loading:AlertState()
    data class Success(val data: List<Alert>):AlertState()
    data class Failure(val error:Throwable):AlertState()
}