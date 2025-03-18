package com.example.weatherforecast.home

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
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
        val weather = homeViewModel.currentWeather.observeAsState()
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
                .padding(top=20.dp)
        ){
            Text(
                weather.value?.name?:"",
                fontSize = 20.sp,
                fontStyle = FontStyle.Italic
            )
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                "${weather.value?.main?.temp?.toInt()}°C",
                fontSize = 40.sp,
                fontStyle = FontStyle.Italic
            )
            Text(
                weather.value?.weather?.get(0)?.main ?:"",
                fontSize = 10.sp,
                color = Color.Gray
            )
        }
    }
}
