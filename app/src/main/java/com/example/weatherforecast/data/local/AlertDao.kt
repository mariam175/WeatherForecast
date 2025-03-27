package com.example.weatherforecast.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecast.data.model.Alert
import com.example.weatherforecast.data.model.Favourites
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDao {
    @Query("SELECT * FROM alert")
    fun getAllAlerts(): Flow<List<Alert>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addAlert(alert: Alert):Long
    @Delete
    suspend fun deleteAlert(alert: Alert):Int
}