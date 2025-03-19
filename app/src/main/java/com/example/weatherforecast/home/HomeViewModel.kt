package com.example.weatherforecast.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.data.model.WeatherResponse
import com.example.weatherforecast.data.reopsitry.Repositry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(val repo:Repositry) : ViewModel() {
    private val _currentWeather = MutableStateFlow<WeatherResponse>(WeatherResponse.Loading)
    val currentWeather  = _currentWeather.asStateFlow()
    private val _message = MutableSharedFlow<String>()
    val messages = _message.asSharedFlow()

    fun getCurrentWeather(){
        viewModelScope.launch(Dispatchers.IO){
            val res = repo.getCurrentWeather(lon=32.2715 , lat=30.5965)
                res
                    .catch {
                    error->_currentWeather.value = WeatherResponse.Failure(error)
                    }
                    .collect{
                        _currentWeather.value = WeatherResponse.Success(it)
                    }

            }
        }
    }



class HomeViewModelFactory(val repo: Repositry) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(repo) as T
    }
}