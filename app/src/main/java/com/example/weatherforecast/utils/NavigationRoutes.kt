package com.example.weatherforecast.utils

import android.location.Location
import androidx.compose.ui.res.stringResource
import com.example.weatherforecast.R
import kotlinx.serialization.Serializable

@Serializable
sealed class NavigationRoutes(
    val title:Int,
    val icon:Int,
){
    @Serializable
    object Home : NavigationRoutes(
        title =  R.string.Home,
        icon = R.drawable.home)
    @Serializable
    object Favourites: NavigationRoutes(
        title = R.string.Favourites ,
        icon = R.drawable.favorite)
    @Serializable
    object Alerts : NavigationRoutes(
        title = R.string.Alerts ,
        icon = R.drawable.notifications)
    @Serializable
    object Settings : NavigationRoutes(
        title = R.string.Settings ,
        icon = R.drawable.settings)
    @Serializable
    class MapScreen(val isFav:Boolean):NavigationRoutes(
        title = 0,
        icon = 0
    )
    @Serializable
    class FavCitiesWeather(val lat:Double , val lon:Double , val city:String):NavigationRoutes(
        title = 0,
        icon = 0
    )
}