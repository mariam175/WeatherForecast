package com.example.weatherforecast.favourites

import androidx.lifecycle.asLiveData
import com.example.weatherforecast.data.model.Favourites
import com.example.weatherforecast.data.reopsitry.Repositry
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import getOrAwaitValue
import io.mockk.coVerify
import io.mockk.verify
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat

class FavouritesViewModelTest{
    lateinit var repositry: Repositry
    lateinit var favouritesViewModel: FavouritesViewModel

    @Before
    fun setup(){
        repositry = mockk(relaxed = true)
        favouritesViewModel = FavouritesViewModel(repositry)
    }
    @Test
    fun getAllFavCities_returnNotNull(){
        //When
        favouritesViewModel.getAllFavCities()
        //Then
        val res = favouritesViewModel.cities
        assertThat(res , not(nullValue()))
    }
    @Test
    fun deleteFavCity(){
        //Given
        val city = Favourites(1 , "ismailia" , 30.00 , 32.00)
        //When
        favouritesViewModel.deleteCity(city)
        //Then
        coVerify { repositry.deleteCity(city) }
    }
}