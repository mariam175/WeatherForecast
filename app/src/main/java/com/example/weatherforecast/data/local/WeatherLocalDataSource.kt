package com.example.weatherforecast.data.local

import com.example.weatherforecast.data.model.Alert
import com.example.weatherforecast.data.model.Favourites
import kotlinx.coroutines.flow.Flow


class WeatherLocalDataSource (val dao: FavCitiesDao) : IWeatherLocalDataSource {
    override fun getAllFavCites() : Flow<List<Favourites>> {
        return dao.getAllFavCities()
    }
    override suspend fun addCity(city:Favourites):Long{
        return dao.addCityToFavourites(city)
    }
    override suspend fun deleteCity(city: Favourites):Int{
        return dao.deleteCityFromFavourites(city)
    }
    override fun getAllAlerts() : Flow<List<Alert>> {
        return dao.getAllAlerts()
    }
    override suspend fun addAlert(alert: Alert):Long{
        return dao.addAlert(alert)
    }
    override suspend fun deleteAlert(alert: Alert):Int{
        return dao.deleteAlert(alert)
    }
     override fun getAlertById(id:Long):Flow<Alert>{
        return dao.getAlertById(id)
    }
}