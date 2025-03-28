package com.example.weatherforecast.data.repositry

import com.example.weatherforecast.data.local.FakeWeatherlocalDataSourceTest
import com.example.weatherforecast.data.local.IWeatherLocalDataSource
import com.example.weatherforecast.data.model.Favourites
import com.example.weatherforecast.data.remote.FakeWeatherRemoteDataSourceTest
import com.example.weatherforecast.data.remote.IWeatherRemoteDataSource
import com.example.weatherforecast.data.reopsitry.Repositry
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Test


class RepositryTest {
    lateinit var localDataSource: IWeatherLocalDataSource
    lateinit var remoteDataSource: IWeatherRemoteDataSource
    lateinit var repositry: Repositry
    val cities = mutableListOf(Favourites(1 , "Globe" , 0.0 , 0.0))
    val favs = MutableStateFlow<List<Favourites>>(cities)

    @Before
    fun setup(){
        localDataSource = FakeWeatherlocalDataSourceTest(favs)
        remoteDataSource = FakeWeatherRemoteDataSourceTest()
        repositry = Repositry(remoteDataSource , localDataSource)
    }
    @Test
    fun addCity_city_returnIsCityAdded() = runTest{
        //Given
        val city = Favourites(
            id = 2,
            city = "ismailia",
            lat = 30.5965,
            lon = 32.2715
        )
        //When
        val res = repositry.addCity(city)
        //Then
        assertThat(res, `is`(city.id.toLong()))
    }
    @Test
    fun getCurrentWeathe_Latlon_returnCurrentWeather() = runTest{
        //Given
        val lat = 40.7128
        val lon = 74.0060
        val lang = "ar"

        //when
        val res = repositry.getCurrentWeather(lat , lon).first()
        //Then
        assertThat(res.weather.get(0).description ,`is`("Clear sky"))
    }

}