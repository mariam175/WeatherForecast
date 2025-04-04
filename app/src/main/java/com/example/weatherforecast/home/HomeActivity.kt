package com.example.weatherforecast.home


import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.RequestState
import com.example.weatherforecast.R
import com.example.weatherforecast.data.model.CurrentWeather
import com.example.weatherforecast.data.model.DailyAndHourlyWeather
import com.example.weatherforecast.data.model.DailyWeatherResponse
import com.example.weatherforecast.data.model.WeatherResponse
import com.example.weatherforecast.data.remote.RetrofitHelper
import com.example.weatherforecast.data.remote.WeatherRemoteDataSource
import com.example.weatherforecast.data.reopsitry.Repositry
import com.example.weatherforecast.utils.CheckNetwork
import com.example.weatherforecast.utils.ICON_URL
import com.example.weatherforecast.utils.WeatherConditions
import com.example.weatherforecast.utils.convertDate

import com.example.weatherforecast.utils.convertToHour
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority


@SuppressLint("SuspiciousIndentation")
@Composable
    fun Home(navHostController: NavHostController,homeViewModel: HomeViewModel , gpsLocation: Location) {
        val lang = homeViewModel.getLanguage()
        val unit = homeViewModel.getUnit()
        homeViewModel.getWindSpeed()
        val mapLocation = homeViewModel.getLocationFromPref()
        val locType = homeViewModel.getLocatioType()
        val context = LocalContext.current
       if(CheckNetwork.checkNetwork(context)){
           LaunchedEffect (gpsLocation , lang , unit){
               when(locType){
                   "gps"->{
                       if (gpsLocation.latitude != 0.0 && gpsLocation.longitude != 0.0) {
                           homeViewModel.getCurrentWeather(lat = gpsLocation.latitude , lon = gpsLocation.longitude , lang = lang , unit=unit)
                           homeViewModel.getDailyWeather(lat = gpsLocation.latitude , lon = gpsLocation.longitude , lang=lang , unit=unit)
                           homeViewModel.saveCurrentLocation(gpsLocation.latitude , gpsLocation.longitude , context)
                       }
                   }
                   "map"->{
                       homeViewModel.getCurrentWeather(lat = mapLocation.first , lon = mapLocation.second , lang = lang , unit=unit)
                       homeViewModel.getDailyWeather(lat = mapLocation.first , lon = mapLocation.second , lang=lang , unit=unit)
                       homeViewModel.saveCurrentLocation(mapLocation.first ,  mapLocation.second , context)
                   }
               }
           }
       }
    else{
        homeViewModel.loadOfflineWeather()
       }

        val weatherState by homeViewModel.currentWeather.collectAsState()
        val dailyState by homeViewModel.dailyWeather.collectAsState()
    val windSpeed by homeViewModel.speed.collectAsState()
    val unitSymbole = homeViewModel.getSympole()
        when (weatherState) {
            is WeatherResponse.Loading -> {
                Loading()
            }
            is WeatherResponse.Success -> {
                val currentWeather = (weatherState as WeatherResponse.Success).data
               homeViewModel.saveCurrentWeather(currentWeather)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CurrentWeather(currentWeather , windSpeed , unitSymbole)
                    homeViewModel.saveCurrentCity(currentWeather.name)
                    if (dailyState is DailyWeatherResponse.Success) {
                        val dailyWeather = (dailyState as DailyWeatherResponse.Success).data
                            homeViewModel.saveDailyAndHourlyWeather(dailyWeather)
                        Spacer(modifier = Modifier.height(16.dp))
                        DailyList(dailyWeather , unitSymbole)
                    }
                }
            }

            else -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .wrapContentSize()
                ) {
                    Text(dailyState.toString())
                }
            }
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun CurrentWeather(weather: CurrentWeather , unit: String , sympole:String) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier

                .padding(top = 20.dp)
        ) {
            Text(
                convertDate(weather.dt)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Image(
                    contentDescription = "",
                    painter = painterResource(R.drawable.location),
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    weather.name,
                    fontSize = 20.sp,
                    fontStyle = FontStyle.Italic
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                "${weather.main.temp.toInt()}°$sympole",
                fontSize = 40.sp,
                fontStyle = FontStyle.Italic
            )
            GlideImage(
                model = WeatherConditions.getWeatherConditions(weather.weather.get(0).icon),
                contentDescription = "",
                Modifier.size(60.dp)
            )
            Text(
                weather.weather.get(0).description,
                fontSize = 10.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))
            WeatherChracteristic(weather , unit)
        }
    }

    @Composable
    fun Loading() {
        Box(
            Modifier
                .fillMaxSize()
                .wrapContentSize()
        ) {
            CircularProgressIndicator()
        }
    }

    @Composable
    fun WeatherChracteristic(weather: CurrentWeather , unit:String) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(60.dp),
            modifier = Modifier
                .padding(16.dp)
                .border(
                    1.dp, Color.Black,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp)
        ) {
            CharactersItem(
                img = R.drawable.wind,
                value = "${weather.wind.speed}",
                unit = unit
            )
            CharactersItem(
                img = R.drawable.pressure,
                value = "${weather.main.pressure}",
                unit = stringResource(R.string.unit_hpa)
            )

            CharactersItem(
                img = R.drawable.humidty,
                value = "${weather.main.humidity}",
                unit = "%"
            )
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun CharactersItem(img: Int, value: String, unit: String) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            GlideImage(
                contentDescription = "",
                model = img,
                modifier = Modifier.size(30.dp)
            )
            Text(value)
            Text(unit)
        }
    }

    @Composable
    fun DailyList(weather: DailyAndHourlyWeather , sympole: String) {
        val groupedByDay = weather.list.groupBy { convertDate(it.dt) }
        val todayHours = groupedByDay.get(convertDate(weather.list.get(0).dt))
        val days = groupedByDay.keys.toList()
       Column{
           Text(
               stringResource(R.string.three_hours_forecast)
           )
           Spacer(modifier = Modifier.height(8.dp))
           LazyRow(
               horizontalArrangement = Arrangement.spacedBy(12.dp)
           ) {
               items(todayHours?.size?:0) {
                   HourlyItem(todayHours!!.get(it) , sympole)
               }
           }
           Spacer(modifier = Modifier.height(16.dp))
           Text(
              stringResource(R.string.five_days_forecast)
           )
           Spacer(modifier = Modifier.height(8.dp))
           Column (
               verticalArrangement = Arrangement.spacedBy(12.dp),
           ){
               days.forEach {
                   val day = groupedByDay.get(it)!!
                   DailyItem(it , day , sympole)
               }
           }
       }


    }
    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun HourlyItem(forecast: CurrentWeather , sympole: String) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Text(
                text = convertToHour(forecast.dt),
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic
            )
            GlideImage(
                model = ICON_URL + forecast.weather.get(0).icon + ".png",
                contentDescription = "",
                modifier = Modifier.size(40.dp)
            )
            Text(
                text = "${forecast.main.temp.toInt()}°$sympole",
                fontSize = 16.sp
            )
        }
    }
    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun DailyItem(day : String , forecast: List<CurrentWeather> , sympole: String) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = day,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic
            )
            GlideImage(
                model = ICON_URL + forecast.get(0).weather.get(0).icon + ".png",
                contentDescription = "",
                modifier = Modifier.size(40.dp)
            )
            Text(
                text = "${forecast.get(0).main.temp_min.toInt()}°$sympole/${forecast.get(forecast.size - 1).main.temp_max.toInt()}°$sympole",
                fontSize = 16.sp
            )
        }
    }




