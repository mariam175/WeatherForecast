package com.example.weatherforecast.data.remote

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker.Result.Success
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.weatherforecast.data.reopsitry.Repositry
import com.example.weatherforecast.utils.LangaugeChange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherWorker(
    private val context: Context,
    private val repo:Repositry,
    private val lon:Double,
    private val lat:Double,
    workerParams: WorkerParameters)
    :Worker(
    context, workerParams
) {
    @SuppressLint("RestrictedApi")
    override  fun doWork(): Result {

             try {
                 val lang = LangaugeChange.getLanguageCode(context)
                 repo.getCurrentWeather(lat = lat , lon = lon , lang)
                 repo.getDailyWeather(lat = lat , lon = lon , lang)
                  return Result.Success()
             } catch (e:Exception){
                  return Result.Failure()
             }

    }
}