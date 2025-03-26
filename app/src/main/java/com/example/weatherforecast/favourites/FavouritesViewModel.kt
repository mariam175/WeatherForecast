package com.example.weatherforecast.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.data.local.FavCititesState
import com.example.weatherforecast.data.model.Favourites
import com.example.weatherforecast.data.reopsitry.Repositry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavouritesViewModel(val repo: Repositry) : ViewModel() {
    private val _citites = MutableStateFlow<FavCititesState>(FavCititesState.Loading)
    val cities = _citites.asStateFlow()
    private val _message = MutableSharedFlow<String>()
    val messages = _message.asSharedFlow()
    fun getAllFavCities(){
       viewModelScope.launch (Dispatchers.IO){
           val res = repo.getAllFavCities()
           res
               .catch {
                   e-> _citites.emit(FavCititesState.Failure(e))
                        _message.emit("Try Again")
               }
               .collect{
                   _citites.emit(FavCititesState.Success(it))
               }
       }
    }
    fun deleteCity(city:Favourites){
        viewModelScope.launch (Dispatchers.IO){
            val res = repo.deleteCity(city)
            if (res > 0){
                _message.emit("delete Fav Cities")
            }
            else{
                _message.emit("Fail To Delete")
            }
        }
    }
}
class FavouriteViewModelFactory(val repo: Repositry):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavouritesViewModel(repo) as T
    }
}