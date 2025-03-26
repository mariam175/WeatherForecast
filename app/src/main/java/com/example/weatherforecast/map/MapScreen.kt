package com.example.weatherforecast.map

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.maps.android.compose.GoogleMap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen (navHostController: NavHostController , mapViewModel: MapViewModel , context: Context){
    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    var selectedLocation by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    var cityName by remember { mutableStateOf("") }
    var showBottomSheet by remember { mutableStateOf(false) }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        onMapClick = {
            selectedLocation = Pair(it.latitude, it.longitude)
            cityName = mapViewModel.getCityName(context , it.latitude , it.longitude).toString()
            Log.i("TAG", "Map: ${cityName}")
            showBottomSheet = true
        }

    )
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Selected Location",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = cityName,
                    fontSize = 16.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        showBottomSheet = false
                        selectedLocation?.let {
                            mapViewModel.saveLatLng(context ,
                                selectedLocation!!.first, selectedLocation!!.second)
                        }
                    }
                ) {
                    Text("OK")
                }
            }
        }
    }
}
