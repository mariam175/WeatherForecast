package com.example.weatherforecast.data.local

import com.example.weatherforecast.data.model.CurrentWeather
import com.example.weatherforecast.data.model.Favourites

sealed class FavCititesState {
    data object Loading:FavCititesState()
    data class Success(val data: List<Favourites>):FavCititesState()
    data class Failure(val error:Throwable):FavCititesState()
}