package com.example.weatherforecast.favourites

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.data.local.FavCititesState
import com.example.weatherforecast.data.model.CityWeather
import com.example.weatherforecast.data.model.CurrentWeather
import com.example.weatherforecast.data.model.DailyAndHourlyWeather
import com.example.weatherforecast.data.model.DailyWeatherResponse
import com.example.weatherforecast.data.model.Favourites
import com.example.weatherforecast.data.model.WeatherResponse
import com.example.weatherforecast.data.reopsitry.Repositry
import com.example.weatherforecast.utils.SettingsChanges
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FavouritesViewModel(val repo: Repositry) : ViewModel() {
    private val _citites = MutableStateFlow<FavCititesState>(FavCititesState.Loading)
    val cities = _citites.asStateFlow()
    private val _currentWeather = MutableStateFlow<WeatherResponse>(WeatherResponse.Loading)
    val currentWeather = _currentWeather.asStateFlow()
    private val _dailyWeather = MutableStateFlow<DailyWeatherResponse>(DailyWeatherResponse.Loading)
    val dailyWeather = _dailyWeather.asStateFlow()
    private val _message = MutableSharedFlow<String>()
    val messages = _message.asSharedFlow()
    private val _lang = MutableStateFlow<String>("en")
    val lang = _lang.asStateFlow()
    private val _unit = MutableStateFlow<String>("metric")
    val unit = _unit.asStateFlow()
    private val _speed = MutableStateFlow<String>("m/s")
    val speed = _speed.asStateFlow()
    var city:String? = null
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
    fun getCurrentWeather(lat: Double, lon: Double, lang: String, unit: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repo.getCurrentWeather(lat = lat, lon = lon, lan = lang, unit = unit)

            res
                .catch { error ->
                    _currentWeather.value = WeatherResponse.Failure(error)
                }
                .collect {
                    _currentWeather.value = WeatherResponse.Success(it)
                     city = it.name
                }
        }
    }

    fun getDailyWeather(lat: Double, lon: Double, lang: String, unit: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repo.getDailyWeather(lat = lat, lon = lon, lan = lang, unit = unit)
            res
                .catch { error ->
                    _dailyWeather.value = DailyWeatherResponse.Failure(error)
                }
                .collect {
                    _dailyWeather.value = DailyWeatherResponse.Success(it)
                }
        }
    }
    fun getLanguage(context: Context): String {
        _lang.value = SettingsChanges.getLanguageCode(context)
        return _lang.value
    }

    fun getUnit(context: Context): String {
        _unit.value = SettingsChanges.getUnit(context)
        return _unit.value
    }

    fun getWindSpeed(context: Context) {
        _speed.value = SettingsChanges.getWindSpeed(context)

    }
    fun getSympole(): String {
        val sympol = when (unit.value) {
            "metric" -> "C"
            "standard" -> "K"
            else -> "F"
        }
        return sympol
    }
    fun saveCityWeather(currentWeather: CurrentWeather , dailyAndHourlyWeather: DailyAndHourlyWeather , city: String){
        viewModelScope.launch (Dispatchers.IO){
             repo.saveCityWeather(
                CityWeather(
                    city = city,
                    currentWeather = currentWeather,
                    list = dailyAndHourlyWeather
                )
            )
        }

    }
    fun getCityWeatherByCityName(city:String){
        viewModelScope.launch (Dispatchers.IO){
            val res = repo.getCityWeather(city)
            res
                .catch {
                   e-> _currentWeather.emit(WeatherResponse.Failure(e))
                }
                .collect{
                _currentWeather.emit(WeatherResponse.Success(it.currentWeather))
                _dailyWeather.emit(DailyWeatherResponse.Success(it.list))
            }
        }
    }
    fun deleteCityWeather(city: String){
        viewModelScope.launch (Dispatchers.IO){
            val cityWeather = repo.getCityWeather(city).first()
            val res = repo.deleteCityWeather(cityWeather)
            if (res > 0){
                _message.emit("delete Fav Cities")
            }
            else{
                _message.emit("Fail To Delete")
            }
        }
    }
    fun getCurrentCity():String?{
        return city
    }

}
class FavouriteViewModelFactory(val repo: Repositry):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavouritesViewModel(repo) as T
    }
}