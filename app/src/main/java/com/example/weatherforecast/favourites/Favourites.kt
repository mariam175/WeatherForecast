package com.example.weatherforecast.favourites

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.weatherforecast.utils.NavigationRoutes
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@Composable
fun FavouritesScreen(navHostController: NavHostController){
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ){
        Fav(nav = {
            navHostController.navigate(NavigationRoutes.MapScreen)
        })
    }
}


@Composable
private fun Fav(nav:()->Unit) {

  FloatingActionButton(
      onClick = {
          nav.invoke()
      }
  ) { }
}