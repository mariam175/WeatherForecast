package com.example.weatherforecast.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.weatherforecast.data.model.Alert
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
@SmallTest
class FavCitiesDaoTest {
    private lateinit var database:WeatherDataBase
    private lateinit var dao: FavCitiesDao
    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDataBase::class.java
        ).build()
        dao = database.getFavDao()
    }
    @After
    fun clear() = database.close()

    @Test
    fun getAlertById_returnAlert() = runTest{
        //Given
        val alert = Alert(1 , "ismailia" , "4:00")
        dao.addAlert(alert)

        //When
        val res = dao.getAlertById(1).first()
        //Then
        assertThat(res.cityName ,`is`(alert.cityName))
    }
    @Test
    fun deleteAlert_returnIsDeleted() = runTest{
        //Given
        val alert = Alert(1 , "ismailia" , "4:00")
        dao.addAlert(alert)

        //When
        val res = dao.deleteAlert(alert)
        //Then
        assertThat(res , not(0))
    }
}