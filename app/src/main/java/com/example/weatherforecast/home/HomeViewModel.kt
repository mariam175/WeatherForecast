package com.example.weatherforecast.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.data.model.CurrentWeather
import com.example.weatherforecast.data.reopsitry.Repositry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(val repo:Repositry) : ViewModel() {
    private val _currentWeather = MutableLiveData<CurrentWeather?>()
    val currentWeather : LiveData<CurrentWeather?> = _currentWeather
    private val _message = MutableLiveData<String>()
    val messages : LiveData<String> = _message

    fun getCurrentWeather(){
        viewModelScope.launch(Dispatchers.IO){
            val res = repo.getCurrentWeather(lon=32.2715 , lat=30.5965)
            if(res != null){
                _currentWeather.postValue(res)
            }
        }
    }


}
class HomeViewModelFactory(val repo: Repositry) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(repo) as T
    }
}