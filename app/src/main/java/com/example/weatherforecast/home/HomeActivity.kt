package com.example.weatherforecast.home

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.RequestState
import com.example.weatherforecast.data.model.CurrentWeather
import com.example.weatherforecast.data.model.WeatherResponse
import com.example.weatherforecast.data.remote.RetrofitHelper
import com.example.weatherforecast.data.remote.WeatherRemoteDataSource
import com.example.weatherforecast.data.reopsitry.Repositry

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
           Home(
               viewModel(
                  factory = HomeViewModelFactory(
                      Repositry(
                          WeatherRemoteDataSource(
                              RetrofitHelper.weatherServices
                          )
                      )
                  )
               )
           )
        }
    }
    @Composable
    fun Home(homeViewModel: HomeViewModel){
        homeViewModel.getCurrentWeather()
        val weatherState by homeViewModel.currentWeather.collectAsState()
        when(weatherState){
            is WeatherResponse.Loading ->Loading()
            is WeatherResponse.Success ->{
                CurrentWeather(
                    (weatherState as WeatherResponse.Success).data
                )
            }
            else->{
                Box(
                    Modifier.fillMaxSize()
                        .wrapContentSize()
                ){
                    Text(
                        "Sorry There is a problem ... Try again",
                    )
                }

            }
        }
    }
    @Composable
    fun CurrentWeather(weather: CurrentWeather){
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
                .padding(top=20.dp)
        ){
            Text(
                weather.name,
                fontSize = 20.sp,
                fontStyle = FontStyle.Italic
            )
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                "${weather.main.temp.toInt()}°C",
                fontSize = 40.sp,
                fontStyle = FontStyle.Italic
            )
            Text(
                weather.weather.get(0).description ,
                fontSize = 10.sp,
                color = Color.Gray
            )
        }
    }
    @Composable
    fun Loading(){
        Box(
            Modifier.fillMaxSize()
                .wrapContentSize()
        ){
            CircularProgressIndicator()
        }
    }
}
