package com.example.weatherforecast.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherforecast.data.model.Favourites

@Database(entities = arrayOf(Favourites::class), version = 1)
abstract class FavDataBase : RoomDatabase() {
abstract fun getFavDao():FavCitiesDao
    companion object{
        @Volatile
        private var INSTANCE: FavDataBase? = null
        fun getInstance (context: Context): FavDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, FavDataBase::class.java, "fav")
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}