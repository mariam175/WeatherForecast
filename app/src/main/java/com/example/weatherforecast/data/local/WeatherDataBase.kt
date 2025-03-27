package com.example.weatherforecast.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherforecast.data.model.Alert
import com.example.weatherforecast.data.model.Favourites

@Database(entities = [Favourites::class  , Alert::class], version = 1)
abstract class WeatherDataBase : RoomDatabase() {
abstract fun getFavDao():FavCitiesDao
    companion object{
        @Volatile
        private var INSTANCE: WeatherDataBase? = null
        fun getInstance (context: Context): WeatherDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, WeatherDataBase::class.java, "weather")
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}