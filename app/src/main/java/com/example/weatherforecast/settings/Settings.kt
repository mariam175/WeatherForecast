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

@Composable
fun SettingsScreen(navHostController: NavHostController , settingsViewModel: SettingsViewModel , context: Context) {
    Column (
        modifier = Modifier.fillMaxSize()
            .padding(top = 40.dp),
        verticalArrangement = Arrangement.Center
    ){
        SettingsComponent(context , settingsViewModel)
    }
}

@Composable
fun SettingsComponent(context: Context,settingsViewModel: SettingsViewModel) {
    val currCode = settingsViewModel.getCurrentLanguage(context)
    val currLang = if(currCode == "ar") stringResource(R.string.Arabic) else stringResource(R.string.English)
    val sections = listOf(
        stringResource(R.string.Location) to Pair(R.drawable.placeholder ,
            listOf("GPS", stringResource(R.string.Map))),
        stringResource(R.string.Language) to Pair(R.drawable.translate ,
            listOf(stringResource(R.string.Arabic) , stringResource(R.string.English))),
        stringResource(R.string.Temperature) to Pair(R.drawable.temperature ,
            listOf("Celsius" , "kelvin" , "Fahrenheit")),
        stringResource(R.string.Windspeed) to Pair(R.drawable.windsetting ,
            listOf("m/s" , "mile/h")),
        stringResource(R.string.Notification) to Pair(R.drawable.bell , emptyList())
    )

    val expandedStates = remember { mutableStateMapOf<String, Boolean>() }
    val selectedItems = remember { mutableStateOf(currLang) }
    val updatedLang = rememberUpdatedState(currLang)

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
                                        selected = selectedItems.value == item || updatedLang.value == item,
                                        onClick = {
                                            selectedItems.value = item
                                            val code = if (item == "Arabic") "ar" else "en"
                                            settingsViewModel.changeLanguage(context, code)

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
