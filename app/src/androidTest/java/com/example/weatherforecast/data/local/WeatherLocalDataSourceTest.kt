package com.example.weatherforecast.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.weatherforecast.data.model.Alert
import com.example.weatherforecast.data.model.Favourites
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@MediumTest
class WeatherLocalDataSourceTest{
    private lateinit var database:WeatherDataBase
    private lateinit var dao: WeatherDao
    private lateinit var localDataSource: WeatherLocalDataSource
    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDataBase::class.java
        ).build()
        dao = database.getFavDao()
        localDataSource = WeatherLocalDataSource(dao)
    }
    @After
    fun clear() = database.close()
    @Test
    fun getAlertById_returnAlert() = runTest{
        //Given
        val alert = Alert(1 , "ismailia" , "4:00")
        localDataSource.addAlert(alert)

        //When
        val res = localDataSource.getAlertById(1).first()
        //Then
        assertThat(res.cityName , `is`(alert.cityName))
    }
    @Test
    fun deleteCity_returnIsDeleted() = runTest{
        //Given
        val city = Favourites(1 , "ismailia" , 32.00,30.00)
        localDataSource.addCity(city)

        //When
        val res = localDataSource.deleteCity(city)
        //Then
        assertThat(res , not(0))
    }
}