package com.example.weatherforecast.map

import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.data.local.FavCititesState
import com.example.weatherforecast.data.model.Favourites
import com.example.weatherforecast.data.model.SearchCity
import com.example.weatherforecast.data.model.SearchResponse
import com.example.weatherforecast.data.reopsitry.Repositry
import com.example.weatherforecast.utils.SEARCH_URL
import com.example.weatherforecast.utils.SettingsChanges
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.util.Locale

class MapViewModel(val repo:Repositry) :ViewModel() {
    private val _addMess = MutableSharedFlow<String>()
    val favState =_addMess.asSharedFlow()
    private val _cities = MutableStateFlow<SearchResponse>(SearchResponse.Loading)
    val cities = _cities.asStateFlow()
    private val _searchQuery = MutableSharedFlow<String>(replay = 1 , onBufferOverflow = BufferOverflow.DROP_OLDEST)

    init {
        viewModelScope.launch {
            _searchQuery
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    repo.searchCity(SEARCH_URL, query)
                        .catch { e -> _cities.emit(SearchResponse.Failure(e)) }
                }
                .collect { response ->
                    _cities.value = SearchResponse.Success(response)
                }
        }
    }

    fun updateSearchQuery(query: String) {
        viewModelScope.launch {
            _searchQuery.emit(query)
        }
    }
     fun getCityName(context: Context , lat: Double,long: Double):String? {
        var cityName: String?
        val geoCoder = Geocoder(context, Locale.getDefault())
        val address = geoCoder.getFromLocation(lat, long, 1)
        cityName = address?.get(0)?.adminArea
        if (cityName == null) {
            cityName = address?.get(0)?.locality
            if (cityName == null) {
                cityName = address?.get(0)?.subAdminArea
            }
        }
        return cityName
    }
    fun saveLatLng(context: Context, lat: Double , long: Double){
        SettingsChanges.saveLocation(context , lat , long)
    }
    fun addCityToFav(city:Favourites){
        viewModelScope.launch (Dispatchers.IO){
            val res = repo.addCity(city)
            if(res > 0){
                _addMess.emit("Added Successfully")
            }
            else{
                _addMess.emit("Already Added")
            }
        }
    }
    fun searchCity(city:String ){
        viewModelScope.launch (Dispatchers.IO){
            val res = repo.searchCity(SEARCH_URL,city)
            res
                .catch {
                    e->_cities.emit(SearchResponse.Failure(e))
                }
                .collect{
                    _cities.emit(SearchResponse.Success(it))
                }
        }
    }
}
class MapViewModelFactory(val repo: Repositry):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MapViewModel(repo) as T
    }
}