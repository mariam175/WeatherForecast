package com.example.weatherforecast.data.local

import com.example.weatherforecast.data.model.Alert
import com.example.weatherforecast.data.model.Favourites
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeWeatherlocalDataSourceTest(
    val cities:MutableStateFlow<List<Favourites>> = MutableStateFlow(emptyList<Favourites>())
) : IWeatherLocalDataSource {

    override fun getAllFavCites(): Flow<List<Favourites>> {
       return cities
    }

    override suspend fun addCity(city: Favourites): Long {
       val res = cities.value.toMutableList().add(city)
        return city.id.toLong()
    }

    override suspend fun deleteCity(city: Favourites): Int {
        TODO("Not yet implemented")
    }

    override fun getAllAlerts(): Flow<List<Alert>> {
        TODO("Not yet implemented")
    }

    override suspend fun addAlert(alert: Alert): Long {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlert(alert: Alert): Int {
        TODO("Not yet implemented")
    }

    override fun getAlertById(id: Long): Flow<Alert> {
        TODO("Not yet implemented")
    }
}