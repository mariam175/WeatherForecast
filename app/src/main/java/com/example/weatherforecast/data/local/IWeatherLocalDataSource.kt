package com.example.weatherforecast.data.local

import com.example.weatherforecast.data.model.Alert
import com.example.weatherforecast.data.model.Favourites
import kotlinx.coroutines.flow.Flow

interface IWeatherLocalDataSource {
    fun getAllFavCites(): Flow<List<Favourites>>

    suspend fun addCity(city: Favourites): Long
    suspend fun deleteCity(city: Favourites): Int
    fun getAllAlerts(): Flow<List<Alert>>

    suspend fun addAlert(alert: Alert): Long
    suspend fun deleteAlert(alert: Alert): Int
    fun getAlertById(id: Long): Flow<Alert>
}