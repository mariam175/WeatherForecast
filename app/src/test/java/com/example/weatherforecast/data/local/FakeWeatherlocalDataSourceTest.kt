package com.example.weatherforecast.data.local

import com.example.weatherforecast.data.model.Alert
import com.example.weatherforecast.data.model.CityWeather
import com.example.weatherforecast.data.model.CurrentWeather
import com.example.weatherforecast.data.model.DailyAndHourlyWeather
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

    override suspend fun saveCurrentWeather(currentWeather: CurrentWeather): Long {
        TODO("Not yet implemented")
    }

    override fun getCurrentWeather(): Flow<CurrentWeather> {
        TODO("Not yet implemented")
    }

    override fun getDailyAndHourly(): Flow<DailyAndHourlyWeather> {
        TODO("Not yet implemented")
    }

    override suspend fun saveDailyAndHourly(dailyAndHourlyWeatherEntity: DailyAndHourlyWeather): Long {
        TODO("Not yet implemented")
    }

    override fun getCityWeather(city: String): Flow<CityWeather> {
        TODO("Not yet implemented")
    }

    override suspend fun saveCityWeather(cityWeather: CityWeather) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCityWeather(cityWeather: CityWeather): Int {
        TODO("Not yet implemented")
    }
}