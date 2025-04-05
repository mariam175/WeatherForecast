package com.example.weatherforecast.alerts

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.weatherforecast.R
import com.example.weatherforecast.data.model.Alert
import com.example.weatherforecast.data.model.WeatherResponse
import kotlinx.coroutines.launch
import java.util.Calendar

@Composable
fun AlertsScreen(navHostController: NavHostController , alertsViewModel: AlertsViewModel) {
    val context = LocalContext.current
    alertsViewModel.getAllAlert()
    val alerts by alertsViewModel.alerts.collectAsState()
    Column (
        modifier = Modifier.fillMaxSize()
            .padding(top = 30.dp),
        verticalArrangement =Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.Alerts),
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )
        Box(
            modifier = Modifier.fillMaxSize()
        ){
            when(alerts){
                is WeatherResponse.Loading-> Loading()
                is WeatherResponse.Success<*> ->{
                    AlertList((alerts as WeatherResponse.Success<List<Alert>>).data , alertsViewModel)
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
                    if(alertsViewModel.isEnableNotifi()){
                        showDateTimePicker(context){ time->
                            val alert =  Alert(
                                cityName = alertsViewModel.getCurrentCity(),
                                time = time.time.toString()
                            )
                            alertsViewModel.addAlert(alert){
                                alertsViewModel.scheduleWeatherNotification(context , time , it)
                            }
                            Log.i("TAG", "AlertsScreen: ${alert.cityName}")
                        }
                    }else{
                        Toast.makeText(context,"You disabled Notification ... Enable it" , Toast.LENGTH_SHORT).show()
                    }
                },
                containerColor = MaterialTheme.colorScheme.tertiary
            ) {
                Icon(
                    painter = painterResource(R.drawable.add),
                    contentDescription = "",
                    modifier = Modifier.size(24.dp)
                )
            }

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
                        text = stringResource(R.string.NoAlerts),
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
    val datePickerDialog = DatePickerDialog(
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

    )
    datePickerDialog.datePicker.minDate = System.currentTimeMillis()
    datePickerDialog.show()
}

@Composable
fun Alert(city:String,time:String , delete:()->Unit) {
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
    ){
        Icon(
            painter = painterResource(R.drawable.bell),
            contentDescription = "",
            modifier = Modifier.size(24.dp),
            tint = Color.Unspecified
        )
        Spacer(modifier = Modifier.width(10.dp))
       Column {
           Text(
               city,
               fontWeight = FontWeight.Bold
           )
           Spacer(modifier = Modifier.height(5.dp))
           Text(
               alertTime(time),
               color = Color.Gray
               )
       }
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

 fun showTimePicker(context: Context, calendar: Calendar, onTimeSelected: (Calendar) -> Unit) {
     val currentCalendar = Calendar.getInstance()
    TimePickerDialog(
        context,
        { _, hour, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0)

            if (calendar.timeInMillis >= currentCalendar.timeInMillis) {
                onTimeSelected(calendar)
            } else {
                Toast.makeText(context , "Cannot select past time ... Try Again" , Toast.LENGTH_SHORT).show()
            }
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    ).show()
     Log.i("TAG", "showTimePicker: $")
}
fun alertTime(time: String): String {
    return try {
        val inputFormat = java.text.SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", java.util.Locale.ENGLISH)
        val outputFormat = java.text.SimpleDateFormat("hh:mm a, dd MMM", java.util.Locale.ENGLISH)
        val date = inputFormat.parse(time)
        outputFormat.format(date ?: time)
    } catch (e: Exception) {
        time
    }
}
