package com.example.weatherforecast.alerts

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.weatherforecast.R
import com.example.weatherforecast.data.local.AlertState
import com.example.weatherforecast.data.model.Alert
import kotlinx.coroutines.launch
import java.util.Calendar

@Composable
fun AlertsScreen(navHostController: NavHostController , alertsViewModel: AlertsViewModel) {
    val context = LocalContext.current
    alertsViewModel.getAllAlert()
    val alerts by alertsViewModel.alerts.collectAsState()
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        when(alerts){
            is AlertState.Loading-> Loading()
            is AlertState.Success ->{
                AlertList((alerts as AlertState.Success).data , alertsViewModel)
            }
            else->{
                Text("Try...Again")
            }
        }
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(
                        bottom = 60.dp,
                        end = 10.dp
                    )
                ,
                onClick = {
                    showDateTimePicker(context){ time->
                        alertsViewModel.scheduleWeatherNotification(context , time)
                        alertsViewModel.addAlert(
                           Alert(
                               cityName = alertsViewModel.getCurrentCity(),
                               time = time.time.toString()
                           )
                        )
                    }
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.add),
                    contentDescription = "",
                    modifier = Modifier.size(24.dp)
                )
            }

    }
}

@Composable
fun AlertList(alerts: List<Alert>, alertsViewModel: AlertsViewModel) {
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
            if (alerts.isEmpty()) {
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
                        text = "No Alerts",
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = 30.dp, bottom = 80.dp)
                ) {
                    items(alerts.size) {
                        Alert(alerts.get(it).cityName , alerts.get(it).time) {
                            scope.launch {
                                val result = snackbarHostState.showSnackbar(
                                    message = "Are you sure?",
                                    actionLabel = "Undo",
                                    duration = SnackbarDuration.Short
                                )
                                if (result != SnackbarResult.ActionPerformed) {
                                    alertsViewModel.deleteAlert(alerts.get(it))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun showDateTimePicker(context: Context, onDateTimeSelected: (Calendar) -> Unit) {
    val calendar = Calendar.getInstance()

    DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            showTimePicker(context, calendar) { selectedTime ->
                onDateTimeSelected(selectedTime)
            }
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}

@Composable
fun Alert(city:String,time:String , delete:()->Unit) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
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
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            time,

        )
        Spacer(modifier = Modifier.width(100.dp))
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

 fun showTimePicker(context: Context, calendar: Calendar, onTimeSelected: (Calendar) -> Unit) {
    TimePickerDialog(
        context,
        { _, hour, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0)

            onTimeSelected(calendar)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    ).show()
     Log.i("TAG", "showTimePicker: $")
}