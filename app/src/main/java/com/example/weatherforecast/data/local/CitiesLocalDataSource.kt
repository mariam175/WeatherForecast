package com.example.weatherforecast.data.local

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
}