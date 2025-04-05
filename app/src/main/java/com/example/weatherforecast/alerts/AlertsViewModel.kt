package com.example.weatherforecast.alerts

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.weatherforecast.data.model.Alert
import com.example.weatherforecast.data.model.WeatherResponse
import com.example.weatherforecast.data.reopsitry.Repositry
import com.example.weatherforecast.utils.SettingsChanges
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class AlertsViewModel(val repositry: Repositry , val context: Context):ViewModel() {
    private val _currentWeather = MutableStateFlow<WeatherResponse>(WeatherResponse.Loading)
    val currentWeather  = _currentWeather.asStateFlow()
    private val _alerts = MutableStateFlow<WeatherResponse>(WeatherResponse.Loading)
    val alerts = _alerts.asStateFlow()
    private val _message = MutableSharedFlow<String>()
    val messages = _message.asSharedFlow()
    var lat :Double? = null
    var lon :Double? = null
    fun getCurrentLoction(){
        val res = SettingsChanges.getCurrentLocation(context)
        lat = res.first
        lon = res.second
    }

    fun scheduleWeatherNotification(context: Context, selectedTime: Calendar , alertId:Long) {
        getCurrentLoction()
        val workData = workDataOf(
            "LATITUDE" to lat,
            "LONGITUDE" to lon,
            "AlERT" to alertId
        )
        val delay = selectedTime.timeInMillis - System.currentTimeMillis()

        if (delay > 0) {
            val workRequest = OneTimeWorkRequestBuilder<AlertWorker>()
                .setInputData(workData)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .addTag("alert no.$alertId")
                .build()
            WorkManager.getInstance(context).enqueue(workRequest)
            Log.i("TAG", "scheduleWeatherNotification: ")
        }
    }
    fun getAllAlert(){
        viewModelScope.launch (Dispatchers.IO){
            val res = repositry.getAllAlert()
            res
                .catch {
                    e->_alerts.emit(WeatherResponse.Failure(e))
                }
                .collect() {
                    _alerts.emit(WeatherResponse.Success(it))
                }
        }
    }
    fun addAlert(alert: Alert , onAlertAdded: (Long) -> Unit){
        viewModelScope.launch (Dispatchers.IO){
            val res = repositry.addAlert(alert)
            if (res > 0){
                _message.emit("Added Successfully")
                onAlertAdded(res)
                Log.i("TAG", "addAlert: $res")
            }
            else{
                _message.emit("Already Added")
            }
        }
    }
    fun deleteAlert(alert: Alert){
        viewModelScope.launch (Dispatchers.IO){
            val id = alert.alertId
            val res = repositry.deleteAlert(alert)
            if (res > 0){
                _message.emit("deleted Successfully")
                WorkManager.getInstance(context).cancelAllWorkByTag("alert no.$id")
            }
            else{
                _message.emit("Fail to delete")
            }
        }
    }
    fun getCurrentCity():String{
        val res = SettingsChanges.getCurrentCity(context)
        return res
    }
    fun isEnableNotifi():Boolean{
        return SettingsChanges.getIsNotificationEnable(context)
    }

}
class AlertsViewModelFactory(val repositry: Repositry , val context: Context) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AlertsViewModel(repositry , context) as T
    }
}