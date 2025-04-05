package com.example.weatherforecast.alerts

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.weatherforecast.data.local.WeatherLocalDataSource
import com.example.weatherforecast.data.local.WeatherDataBase
import com.example.weatherforecast.data.remote.RetrofitHelper
import com.example.weatherforecast.data.remote.WeatherRemoteDataSource
import com.example.weatherforecast.data.reopsitry.Repositry
import com.example.weatherforecast.utils.Helper
import com.example.weatherforecast.utils.Notification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class AlertWorker(context: Context,
                  workerParams: WorkerParameters )
    :CoroutineWorker (context , workerParams){

    @SuppressLint("RestrictedApi", "SuspiciousIndentation")
    override suspend fun doWork(): Result {
        val lat = inputData.getDouble("LATITUDE", 0.0)
        val lon = inputData.getDouble("LONGITUDE", 0.0)
        val alertId = inputData.getLong("AlERT" , 0)
        Log.i("TAG", "doWork: $lat $lon")
        val repo = Repositry(
            WeatherRemoteDataSource(RetrofitHelper.weatherServices),
            WeatherLocalDataSource(WeatherDataBase.getInstance(applicationContext).getFavDao())
        )
        val alert = withContext(Dispatchers.IO) { repo.getAlertById(alertId).firstOrNull() }
        Log.i("TAG", "doWork: ${alert?.alertId?:0} - $alertId")
        val isConnected = Helper.checkNetwork(applicationContext)
       if (isConnected){
           val curr = repo.getCurrentWeather(lat , lon)
           curr
               .catch {
                   Result.failure()
               }
               .collect{
                   Notification.showNotification("its ${it.weather.get(0).description} in ${it.name}" , applicationContext)
               }
       }else{
           Notification.showNotification("Please open Network to check the weather" , applicationContext)
       }
        withContext(Dispatchers.IO) {
            if(alert != null){
                val del = repo.deleteAlert(alert)
                if (del > 0) Log.i("TAG", "doWork: Alert deleted from Room")
                else Log.i("TAG", "doWork: Alert not found in Room")
            }

        }

        return Result.Success()
    }
}