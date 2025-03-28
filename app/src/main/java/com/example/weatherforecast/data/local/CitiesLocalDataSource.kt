package com.example.weatherforecast.data.local

import com.example.weatherforecast.data.model.Alert
import com.example.weatherforecast.data.model.Favourites
import kotlinx.coroutines.flow.Flow


class CitiesLocalDataSource (val dao: FavCitiesDao){
    fun getAllFavCites() : Flow<List<Favourites>> {
        return dao.getAllFavCities()
    }
    suspend fun addCity(city:Favourites):Long{
        return dao.addCityToFavourites(city)
    }
    suspend fun deleteCity(city: Favourites):Int{
        return dao.deleteCityFromFavourites(city)
    }
    fun getAllAlerts() : Flow<List<Alert>> {
        return dao.getAllAlerts()
    }
    suspend fun addAlert(alert: Alert):Long{
        return dao.addAlert(alert)
    }
    suspend fun deleteAlert(alert: Alert):Int{
        return dao.deleteAlert(alert)
    }
     fun getAlertById(id:Long):Flow<Alert>{
        return dao.getAlertById(id)
    }
}