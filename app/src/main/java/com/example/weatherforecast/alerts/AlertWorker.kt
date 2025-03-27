package com.example.weatherforecast.alerts

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.weatherforecast.data.local.CitiesLocalDataSource
import com.example.weatherforecast.data.local.WeatherDataBase
import com.example.weatherforecast.data.remote.RetrofitHelper
import com.example.weatherforecast.data.remote.WeatherRemoteDataSource
import com.example.weatherforecast.data.reopsitry.Repositry
import com.example.weatherforecast.utils.Notification
import kotlinx.coroutines.flow.catch

class AlertWorker(context: Context,
                  workerParams: WorkerParameters )
    :CoroutineWorker (context , workerParams){

    @SuppressLint("RestrictedApi", "SuspiciousIndentation")
    override suspend fun doWork(): Result {
        val lat = inputData.getDouble("LATITUDE", 0.0)
        val lon = inputData.getDouble("LONGITUDE", 0.0)
        Log.i("TAG", "doWork: $lat $lon")
        val repo = Repositry(
            WeatherRemoteDataSource(RetrofitHelper.weatherServices),
            CitiesLocalDataSource(WeatherDataBase.getInstance(applicationContext).getFavDao())
        )
        val curr = repo.getCurrentWeather(lat , lon)
            curr
               .catch {
                   Result.failure()
               }
               .collect{
              Notification.showNotification(it.weather.get(0).description , applicationContext)
           }


        return Result.Success()
    }
}