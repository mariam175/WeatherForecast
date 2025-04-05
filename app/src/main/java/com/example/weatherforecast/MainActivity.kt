package com.example.weatherforecast

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.weatherforecast.alerts.AlertsScreen
import com.example.weatherforecast.alerts.AlertsViewModelFactory
import com.example.weatherforecast.data.local.WeatherLocalDataSource
import com.example.weatherforecast.data.local.WeatherDataBase
import com.example.weatherforecast.data.remote.RetrofitHelper
import com.example.weatherforecast.data.remote.WeatherRemoteDataSource
import com.example.weatherforecast.data.reopsitry.Repositry
import com.example.weatherforecast.favourites.FavouriteViewModelFactory
import com.example.weatherforecast.favourites.view.FavCitiesWeather
import com.example.weatherforecast.favourites.view.FavouritesScreen
import com.example.weatherforecast.home.Home
import com.example.weatherforecast.home.HomeViewModelFactory
import com.example.weatherforecast.map.MapScreen
import com.example.weatherforecast.map.MapScreenOSM
import com.example.weatherforecast.map.MapViewModelFactory
import com.example.weatherforecast.settings.SettingsScreen
import com.example.weatherforecast.ui.theme.WeatherForecastTheme
import com.example.weatherforecast.utils.SettingsChanges
import com.example.weatherforecast.utils.NavigationRoutes
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class MainActivity : ComponentActivity() {
    private  val LOCATION_ID = 550
    private lateinit var fusedLoction: FusedLocationProviderClient
    private lateinit var loctState: MutableState<Location>
    private  val TAG = "location"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lang =  mutableStateOf(SettingsChanges.getLanguageCode(this))
        loctState =  mutableStateOf(Location(""))
         mutableStateOf( SettingsChanges.applyLanguage(this , lang.value))
        setContent {

           WeatherForecastTheme {
               AppScreen(loctState.value)
           }
        }
    }
    override fun onStart() {
        super.onStart()
        if (checkPremission()){
            if(isLocationEnable()){
                getLoction()
            }
            else{
                enableLoction()
            }
        }
        else{
            ActivityCompat.requestPermissions(
                this ,
                arrayOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ),
                LOCATION_ID
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
        if (requestCode == LOCATION_ID) {
            if ( grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Log.i(TAG, "Location permission granted! Fetching location...")
                getLoction()
            }
        }
    }

    fun checkPremission() : Boolean{
        return checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) ==  PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) ==  PackageManager.PERMISSION_GRANTED
    }
    fun isLocationEnable() : Boolean{
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

    fun enableLoction(){
        val setting: Intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(setting)
    }

    @SuppressLint("MissingPermission")

    fun getLoction(){
        fusedLoction = LocationServices.getFusedLocationProviderClient(this)
        fusedLoction.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                loctState.value = it
            }
        }
        fusedLoction.requestLocationUpdates(
            LocationRequest.Builder(0).apply {
                setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            }.build(),
            object : LocationCallback(){
                override fun onLocationResult(locationResult: LocationResult)
                {
                    super.onLocationResult(locationResult)
                    Log.i(TAG, "Location lattiaude"+locationResult.lastLocation?.latitude.toString()?:"loc")
                    loctState.value = locationResult.lastLocation ?: Location("")
                }
            },
            Looper.myLooper()
        )
    }
    @Composable
    fun AppScreen(location: Location) {
        val navController = rememberNavController()
        Scaffold(bottomBar = {
            BottomBar(navController) },

            modifier = Modifier.fillMaxSize()
        ) { padd ->
            Log.i("TAG", "AppScreen: $padd")
            BottomNavGraph(navController , location , Modifier.padding(padd))
        }
    }

    @Composable
    fun BottomBar(navHostController: NavHostController) {
        val listOfScreens = arrayOf<NavigationRoutes>(
           NavigationRoutes.Home,
            NavigationRoutes.Favourites,
            NavigationRoutes.Alerts,
            NavigationRoutes.Settings
        )
        val navBackStackEntry = navHostController.currentBackStackEntryAsState()

        val selectedItem = remember { mutableStateOf(0) }
        NavigationBar (
            modifier = Modifier.height(60.dp)
        ){
            listOfScreens.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            painterResource(
                                item.icon,
                            ),
                            contentDescription = "",
                            modifier = Modifier.size(15.dp),
                        )
                    },
                    label = { Text(stringResource(item.title)) },
                    selected = selectedItem.value == index,
                    onClick = {
                        selectedItem.value = index

                        navHostController.navigate(item) {
                            popUpTo(navHostController.graph.findStartDestination().id)
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }



    @Composable
    fun BottomNavGraph(navHostController: NavHostController , location:Location , modifier: Modifier) {
        val context = LocalContext.current
        NavHost(
            navController = navHostController,
            startDestination = NavigationRoutes.Home,
            modifier = modifier) {
            composable<NavigationRoutes.Home>() {
                    navHostController.getBackStackEntry(NavigationRoutes.Home)
                Home(navHostController, viewModel(
                    factory = HomeViewModelFactory(
                        Repositry(
                            WeatherRemoteDataSource(
                                RetrofitHelper.weatherServices
                            ),
                            WeatherLocalDataSource(
                                WeatherDataBase.getInstance(context).getFavDao()
                            )
                        ),context
                    )
                ) , location)

            }
            composable<NavigationRoutes.Favourites>() {
                FavouritesScreen(navHostController ,
                    viewModel(
                        factory = FavouriteViewModelFactory(
                            Repositry(
                                WeatherRemoteDataSource(
                                    RetrofitHelper.weatherServices
                                ),
                                WeatherLocalDataSource(
                                    WeatherDataBase.getInstance(context).getFavDao()
                                )
                            )
                        )
                    ))
            }
            composable<NavigationRoutes.Alerts>() {
                AlertsScreen(navHostController , viewModel(
                    factory = AlertsViewModelFactory(
                        Repositry(
                            WeatherRemoteDataSource(
                                RetrofitHelper.weatherServices
                            ),
                            WeatherLocalDataSource(
                                WeatherDataBase.getInstance(context).getFavDao()
                            )
                        ),context
                    )
                    )
                )
            }
            composable<NavigationRoutes.Settings>() {

                SettingsScreen(navHostController ,
                    viewModel(),
                    context
                )
            }
            composable<NavigationRoutes.MapScreen>() {
                backStackEntry->
                val isFav = backStackEntry.toRoute<NavigationRoutes.MapScreen>().isFav
                MapScreenOSM(navHostController ,
                    viewModel(
                        factory = MapViewModelFactory(
                            Repositry(
                                WeatherRemoteDataSource(
                                    RetrofitHelper.weatherServices
                                ),
                                WeatherLocalDataSource(
                                    WeatherDataBase.getInstance(context).getFavDao()
                                )
                            )
                        )
                    ) ,
                     isFav)
            }
            composable<NavigationRoutes.FavCitiesWeather>() {
                    backStackEntry->
                val lat = backStackEntry.toRoute<NavigationRoutes.FavCitiesWeather>().lat
                val lon = backStackEntry.toRoute<NavigationRoutes.FavCitiesWeather>().lon
                val city = backStackEntry.toRoute<NavigationRoutes.FavCitiesWeather>().city
                FavCitiesWeather(navHostController ,
                    viewModel(
                        factory = FavouriteViewModelFactory(
                            Repositry(
                                WeatherRemoteDataSource(
                                    RetrofitHelper.weatherServices
                                ),
                                WeatherLocalDataSource(
                                    WeatherDataBase.getInstance(context).getFavDao()
                                )
                            )
                        )
                    ) ,
                    lat , lon , city)
            }
        }
    }

}
