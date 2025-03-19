package com.example.weatherforecast.home

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.RequestState
import com.example.weatherforecast.R
import com.example.weatherforecast.data.model.CurrentWeather
import com.example.weatherforecast.data.model.WeatherResponse
import com.example.weatherforecast.data.remote.RetrofitHelper
import com.example.weatherforecast.data.remote.WeatherRemoteDataSource
import com.example.weatherforecast.data.reopsitry.Repositry
import com.example.weatherforecast.utils.ICON_URL
import com.example.weatherforecast.utils.convertDate

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
                    Modifier
                        .fillMaxSize()
                        .wrapContentSize()
                ){
                    Text(
                        "Sorry There is a problem ... Try again",
                    )
                }

            }
        }
    }
    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun CurrentWeather(weather: CurrentWeather){
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp)
        ){
            Text(
                convertDate(weather.dt)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row (
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ){
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
                "${weather.main.temp.toInt()}°C",
                fontSize = 40.sp,
                fontStyle = FontStyle.Italic
            )
            GlideImage(
                model = ICON_URL+weather.weather.get(0).icon+".png",
                contentDescription = "",
                Modifier.size(60.dp)
            )
            Text(
                weather.weather.get(0).description ,
                fontSize = 10.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(40.dp))
            WeatherChracteristic(weather)
        }
    }
    @Composable
    fun Loading(){
        Box(
            Modifier
                .fillMaxSize()
                .wrapContentSize()
        ){
            CircularProgressIndicator()
        }
    }
    @Composable
    fun WeatherChracteristic(weather: CurrentWeather){
        Row(
            horizontalArrangement = Arrangement.spacedBy(60.dp),
            modifier = Modifier
                .padding(16.dp)
                .border(
                    1.dp, Color.Black,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp)
        ){
                CharactersItem(
                    img = R.drawable.wind,
                    value = "${weather.wind.speed}",
                    unit = "m/s"
                )
            CharactersItem(
                img = R.drawable.pressure,
                value = "${weather.main.pressure}",
                unit = "hpa"
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
    fun CharactersItem(img:Int , value:String , unit:String) {
        Column (
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ){
            GlideImage(
                contentDescription = "",
                model = img,
                modifier = Modifier.size(30.dp)
            )
            Text(value)
            Text(unit)
        }
    }
}
