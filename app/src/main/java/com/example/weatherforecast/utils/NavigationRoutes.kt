package com.example.weatherforecast.utils

import com.example.weatherforecast.R
import kotlinx.serialization.Serializable

@Serializable
sealed class NavigationRoutes(
    val title:String,
    val icon:Int
){
    @Serializable
    object Home : NavigationRoutes(
        title = "Home" ,
        icon = R.drawable.home)
    @Serializable
    object Favourites : NavigationRoutes(
        title = "Favourites" ,
        icon = R.drawable.favorite)
    @Serializable
    object Alerts : NavigationRoutes(
        title = "Alerts" ,
        icon = R.drawable.notifications)
    @Serializable
    object Settings : NavigationRoutes(
        title = "Settings" ,
        icon = R.drawable.settings)
}