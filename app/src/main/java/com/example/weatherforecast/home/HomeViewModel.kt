package com.example.weatherforecast.home


import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.data.model.DailyWeatherResponse
import com.example.weatherforecast.data.model.WeatherResponse
import com.example.weatherforecast.data.reopsitry.Repositry
import com.example.weatherforecast.utils.LangaugeChange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(val repo:Repositry , context: Context) : ViewModel() {
    private val _currentWeather = MutableStateFlow<WeatherResponse>(WeatherResponse.Loading)
    val currentWeather  = _currentWeather.asStateFlow()
    private val _dailyWeather = MutableStateFlow<DailyWeatherResponse>(DailyWeatherResponse.Loading)
    val dailyWeather  = _dailyWeather.asStateFlow()
    private val _message = MutableSharedFlow<String>()
    val messages = _message.asSharedFlow()

    val lang = LangaugeChange.getLanguageCode(context)
    fun getCurrentWeather(lat:Double , lon:Double){
        viewModelScope.launch(Dispatchers.IO){
            val res = repo.getCurrentWeather(lat = lat, lon = lon , lan=lang)
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
    fun getDailyWeather(lat:Double , lon:Double){
        viewModelScope.launch(Dispatchers.IO){
            val res = repo.getDailyWeather(lat = lat , lon=lon , lan = lang)
            res
                .catch {
                        error->_dailyWeather.value = DailyWeatherResponse.Failure(error)
                }
                .collect{
                    _dailyWeather.value = DailyWeatherResponse.Success(it)
                }
        }
    }
    }

class HomeViewModelFactory(val repo: Repositry , val con: Context) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(repo , context = con) as T
    }
}