package com.example.weatherforecast.settings

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.weatherforecast.R
import com.example.weatherforecast.utils.NavigationRoutes

@Composable
fun SettingsScreen(navHostController: NavHostController , settingsViewModel: SettingsViewModel , context: Context) {
    Column (
        modifier = Modifier.fillMaxSize()
            .padding(top = 40.dp),
        verticalArrangement = Arrangement.Center
    ){
        SettingsComponent(context , settingsViewModel){
            navHostController.navigate(NavigationRoutes.MapScreen(isFav = false))
        }
    }
}

@Composable
fun SettingsComponent(context: Context,settingsViewModel: SettingsViewModel , nav:()->Unit) {
    val currCode = settingsViewModel.getCurrentLanguage(context)
    val currLang = if(currCode == "ar") stringResource(R.string.Arabic) else stringResource(R.string.English)
    val currTemp = settingsViewModel.geUnit(context)
    val currentUnit = if(currTemp == "metric") stringResource(R.string.Celsius) else if(currTemp == "standard") stringResource(R.string.kelvin) else stringResource(R.string.Fahrenheit)
    val currType = settingsViewModel.getLocationType(context)
   val sections = ListOfOptions()
    val expandedStates = remember { mutableStateMapOf<String, Boolean>() }
    val selectedItems = remember { mutableStateMapOf<String, String>() }
    val languageKey = stringResource(R.string.Language)
    val temperatureKey = stringResource(R.string.Temperature)
    val speedKey = stringResource(R.string.Windspeed)
    val locKey = stringResource(R.string.Location)
    val notifi = stringResource(R.string.Notification)
    selectedItems[languageKey] = currLang
    selectedItems[temperatureKey] = currentUnit
    selectedItems[locKey] = if (currType == "gps") "GPS" else stringResource(R.string.Map)
    val currSpeed = settingsViewModel.geWindSpeed(context)
    val isNotifiEnable = settingsViewModel.getIsNotificationEnable(context)
    selectedItems[speedKey] = currSpeed
    selectedItems[notifi] = if(isNotifiEnable) stringResource(R.string.Enable) else stringResource(R.string.Disable)
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        sections.forEach { (title, pair) ->
            item {
                val (icon , items) = pair
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            expandedStates[title] = !(expandedStates[title] ?: false)
                        }
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = icon),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = androidx.compose.ui.graphics.Color.Unspecified
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            painter = painterResource(
                                id = if (expandedStates[title] == true)
                                 R.drawable.arrow_up else R.drawable.arrow_down
                            ),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }


                    if (expandedStates[title] == true) {
                        Column(modifier = Modifier.padding(
                            start = 32.dp,
                            top = 8.dp)) {
                            items.forEach { item ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = selectedItems[title] == item,
                                        onClick = {
                                            selectedItems[title] = item
                                            when (title) {
                                                languageKey -> {
                                                    val code = if (item == context.getString(R.string.Arabic)) "ar" else "en"
                                                    settingsViewModel.changeLanguage(context, code)
                                                }
                                                temperatureKey -> {
                                                    val unit = changeTempUnit(item, context)
                                                    settingsViewModel.changeUnit(context, unit)
                                                    val newSpeed = changeWindSpeed(item, context)
                                                    selectedItems[speedKey] = newSpeed
                                                    settingsViewModel.changeWindSpeed(context, newSpeed)
                                                }
                                                speedKey->{
//                                                    val speed = changeWindSpeed(temperatureKey , context)
//                                                    settingsViewModel.changeWindSpeed(context , speed)
                                                }
                                                locKey->{
                                                    val type = if (item == context.getString(R.string.Map)) "map" else "gps"
                                                    settingsViewModel.changeLocationType(context, type)
                                                    if (item == context.getString(R.string.Map)){
                                                        nav.invoke()
                                                    }
                                                }
                                                notifi->{
                                                    val isEnable = if (item == context.getString(R.string.Enable)) true else false
                                                    settingsViewModel.isNotificationEnable(context , isEnable)

                                                }
                                            }
                                        }
                                    )
                                    Text(item, modifier = Modifier.padding(start = 8.dp))
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
fun ListOfOptions() :  List<Pair<String, Pair<Int, List<String>>>>{
    val sections = listOf(
        stringResource(R.string.Location) to Pair(R.drawable.placeholder ,
            listOf("GPS", stringResource(R.string.Map))),
        stringResource(R.string.Language) to Pair(R.drawable.translate ,
            listOf(
                stringResource(R.string.Arabic) ,
                stringResource(R.string.English))),
        stringResource(R.string.Temperature) to Pair(R.drawable.temperature ,
            listOf(
                stringResource(R.string.Celsius),
                stringResource(R.string.kelvin) ,
                stringResource(R.string.Fahrenheit)
            )),
        stringResource(R.string.Windspeed) to Pair(R.drawable.windsetting ,
            listOf(stringResource(R.string.unit_m_s) , stringResource(R.string.mileh))),
        stringResource(R.string.Notification) to Pair(R.drawable.bell ,
            listOf(stringResource(R.string.Enable) ,
                stringResource(R.string.Disable))),
    )
    return sections
}

fun changeTempUnit(item : String , context: Context):String{
    var unit = ""
    when (item) {
        context.getString(R.string.Fahrenheit) -> unit  = "imperial"
        context.getString(R.string.kelvin) -> unit = "standard"
        else -> unit = "metric"
    }
    return unit
}
fun changeWindSpeed(curr:String , context: Context):String{
    var speed = ""
    if (curr == context.getString(R.string.kelvin)){
        speed = context.getString(R.string.mileh)
    }
    else{
        speed = context.getString(R.string.unit_m_s)
    }
    return speed
}
