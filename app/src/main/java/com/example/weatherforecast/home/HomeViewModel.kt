package com.example.weatherforecast.home


import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.data.model.DailyWeatherResponse
import com.example.weatherforecast.data.model.WeatherResponse
import com.example.weatherforecast.data.reopsitry.Repositry
import com.example.weatherforecast.utils.SettingsChanges
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(val repo:Repositry , val context: Context) : ViewModel() {
    private val _currentWeather = MutableStateFlow<WeatherResponse>(WeatherResponse.Loading)
    val currentWeather  = _currentWeather.asStateFlow()
    private val _dailyWeather = MutableStateFlow<DailyWeatherResponse>(DailyWeatherResponse.Loading)
    val dailyWeather  = _dailyWeather.asStateFlow()
    private val _message = MutableSharedFlow<String>()
    val messages = _message.asSharedFlow()
    private val _lang = MutableStateFlow<String>("en")
    val lang  = _lang.asStateFlow()
    private val _unit = MutableStateFlow<String>("metric")
    val unit  = _unit.asStateFlow()
    private val _speed = MutableStateFlow<String>("m/s")
    val speed  = _speed.asStateFlow()

    fun getLanguage() : String{
       _lang.value = SettingsChanges.getLanguageCode(context)
        return _lang.value
    }
    fun getUnit() : String{
        _unit.value = SettingsChanges.getUnit(context)
        return _unit.value
    }
    fun getWindSpeed(){
        _speed.value = SettingsChanges.getWindSpeed(context)

    }
    fun getSympole():String{
        val sympol = when(unit.value){
            "metric" -> "C"
             "standard" -> "K"
            else->"F"
        }
        return sympol
    }
    fun getCurrentWeather(lat:Double , lon:Double , lang:String , unit:String){
        viewModelScope.launch(Dispatchers.IO){
            val res = repo.getCurrentWeather(lat = lat, lon = lon , lan=lang , unit=unit)
            Log.i("TAG", "getCurrentWeather: $lang")
                res
                    .catch {
                    error->_currentWeather.value = WeatherResponse.Failure(error)
                    }
                    .collect{
                        _currentWeather.value = WeatherResponse.Success(it)
                    }
            }
        }
    fun getDailyWeather(lat:Double , lon:Double , lang:String , unit: String){
        viewModelScope.launch(Dispatchers.IO){
            val res = repo.getDailyWeather(lat = lat , lon=lon , lan = lang , unit = unit)
            res
                .catch {
                        error->_dailyWeather.value = DailyWeatherResponse.Failure(error)
                }
                .collect{
                    _dailyWeather.value = DailyWeatherResponse.Success(it)
                }
        }
    }
    fun getLocationFromPref():Pair<Double,Double>{
        return SettingsChanges.getLatLangType(context)
    }
    fun getLocatioType():String{
       return SettingsChanges.getLocationType(context)
    }
}

class HomeViewModelFactory(val repo: Repositry , val con: Context) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(repo , context = con) as T
    }
}