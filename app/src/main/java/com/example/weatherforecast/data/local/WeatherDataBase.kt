package com.example.weatherforecast.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherforecast.data.model.Alert
import com.example.weatherforecast.data.model.CurrentWeather
import com.example.weatherforecast.data.model.DailyAndHourlyWeather
import com.example.weatherforecast.data.model.Favourites

@Database(entities = [Favourites::class  , Alert::class , CurrentWeather::class , DailyAndHourlyWeather::class], version = 3,exportSchema = false)
@TypeConverters(TempConverter::class, WeatherConverter::class, WindConverter::class , ListCurrentWeather::class)
abstract class WeatherDataBase : RoomDatabase() {
abstract fun getFavDao():FavCitiesDao
    companion object{
        @Volatile
        private var INSTANCE: WeatherDataBase? = null
        fun getInstance (context: Context): WeatherDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, WeatherDataBase::class.java, "weatherdb1").fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}