package com.example.weatherforecast.favourites.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.weatherforecast.R
import com.example.weatherforecast.data.local.FavCititesState
import com.example.weatherforecast.data.model.Favourites
import com.example.weatherforecast.favourites.FavouritesViewModel
import com.example.weatherforecast.utils.NavigationRoutes
import kotlinx.coroutines.launch


@Composable
fun FavouritesScreen(navHostController: NavHostController ,favouritesViewModel: FavouritesViewModel){
    Column (
        modifier = Modifier.fillMaxSize()
            .padding(top = 30.dp),
        verticalArrangement =Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            stringResource(R.string.Favourites),
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )

        Fav(favouritesViewModel , nav = {
            navHostController.navigate(NavigationRoutes.MapScreen(isFav = true))
        },navHostController)
    }
}


@SuppressLint("SuspiciousIndentation")
@Composable
private fun Fav(favouritesViewModel: FavouritesViewModel, nav:()->Unit , navHostController: NavHostController) {
    favouritesViewModel.getAllFavCities()
    val cities by favouritesViewModel.cities.collectAsState()
      Box(
         modifier = Modifier.fillMaxSize()
      ){
         when(cities){
             is FavCititesState.Loading-> Loading()
             is FavCititesState.Success ->{
                 FavList((cities as FavCititesState.Success).data , favouritesViewModel , navHostController)
             }
             else->{
                 Text("Try...Again")
             }
         }
          FloatingActionButton(
              modifier = Modifier
                  .align(Alignment.BottomEnd)
                  .padding(bottom = 60.dp,
                      end = 10.dp)
                  ,
              onClick = {
                  nav.invoke()
              },
              containerColor = MaterialTheme.colorScheme.tertiary
          ) {
              Icon(
                  painter = painterResource(R.drawable.heart_plus),
                  contentDescription = "",
                  modifier = Modifier.size(24.dp)
              )
          }
      }
}

@Composable
fun FavList(favItems: List<Favourites>, favouritesViewModel: FavouritesViewModel , navHostController: NavHostController) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (favItems.isEmpty()) {
               Column (
                   horizontalAlignment = Alignment.CenterHorizontally
               ){
                   Image(
                       painterResource(R.drawable.empty),
                       contentDescription = ""
                   )
                   Spacer(
                       modifier = Modifier.height(10.dp)
                   )
                   Text(
                       text = stringResource(R.string.NoFavCities),
                       fontWeight = FontWeight.Bold
                   )
               }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = 30.dp, bottom = 80.dp)
                ) {
                    items(favItems.size) { index ->
                        FavItem(favItems[index].city , nav = {
                            navHostController.navigate(NavigationRoutes.FavCitiesWeather(
                                favItems.get(index).lat , favItems.get(index).lon , favItems.get(index).city
                            ))
                        }) {
                            scope.launch {
                                val result = snackbarHostState.showSnackbar(
                                    message = "Are you sure?",
                                    actionLabel = "Undo",
                                    duration = SnackbarDuration.Short
                                )
                                if (result != SnackbarResult.ActionPerformed) {
                                    favouritesViewModel.deleteCity(favItems[index])
                                    favouritesViewModel.deleteCityWeather(favItems[index].city)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FavItem(city:String  , nav:()->Unit , delete:()->Unit) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp , vertical = 8.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(bottomEnd = 16.dp , bottomStart = 16.dp),
                clip = false
            )
            .clip(
                shape = RoundedCornerShape(bottomEnd = 16.dp , bottomStart = 16.dp)
            )
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
            .clickable {
                nav.invoke()
            }
    ){
        Icon(
            painter = painterResource(R.drawable.placeholder),
            contentDescription = "",
            modifier = Modifier.size(24.dp),
            tint = Color.Unspecified
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            city,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(R.drawable.trash),
            contentDescription = "",

            modifier = Modifier
                .clickable {
                delete.invoke()
            }
                .size(24.dp)

        )
    }
}
@Composable
fun Loading() {
   Box(
       Modifier.fillMaxWidth()
   ){
       CircularProgressIndicator()
   }
}