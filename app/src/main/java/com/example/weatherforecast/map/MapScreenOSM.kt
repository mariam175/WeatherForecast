package com.example.weatherforecast.map

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.weatherforecast.data.model.Favourites
import com.example.weatherforecast.data.model.SearchCity
import com.example.weatherforecast.data.model.SearchResponse
import com.example.weatherforecast.favourites.view.Loading2
import com.google.maps.android.compose.GoogleMap
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker

@Composable
fun MapScreenOSM(navHostController: NavHostController,
                 mapViewModel: MapViewModel,
                 isFav:Boolean){
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    val cities by  mapViewModel.cities.collectAsState()
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    var selectedLocation = remember { mutableStateOf<GeoPoint?>(null) }
    var cityName = remember { mutableStateOf<String>("") }
    var suggestions = remember { mutableStateOf<List<SearchCity>>(emptyList()) }
    var showBottomSheet = remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth(),) {
        Map(navHostController, mapViewModel, context, isFav , selectedLocation.value , showBottomSheet)

        Column(
            modifier = Modifier.align(Alignment.TopCenter),
        ) {
            SearchBar(searchText, suggestions.value, onSearch = {
                mapViewModel.updateSearchQuery(it)
            }, onCitySelected = {
                val newLocation = GeoPoint(it.lat, it.lon)
                selectedLocation.value = newLocation
                cityName.value = it.cityName
                mapView.controller.animateTo(newLocation)
                suggestions.value = emptyList()
                Log.i("City", "MapScreenOSMCity: $cityName")
            })

            when (cities) {
                is SearchResponse.Success -> {
                    suggestions.value = (cities as SearchResponse.Success).data
                    Log.i("TAG", "MapScreenOSM: ${suggestions.value}")

                }

                is SearchResponse.Failure -> {
                    Toast.makeText(
                        context,
                        "Error: ${(cities as SearchResponse.Failure).error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {}
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Map(navHostController: NavHostController ,
        mapViewModel: MapViewModel ,
        context: Context, isFav:Boolean ,
        selectedCity:GeoPoint? ,
        showBottomSheet: MutableState<Boolean>
) {

    val sheetState = rememberModalBottomSheetState()
    val mapView = remember { MapView(context) }
    var selectedLocation by remember { mutableStateOf<GeoPoint?>(null) }
    var cityName by remember { mutableStateOf("") }
    AndroidView(
        factory = { ctx ->
            mapView.apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                controller.setZoom(3.0)
                controller.setCenter(selectedCity)
                overlays.add(MapEventsOverlay(object : MapEventsReceiver {
                    override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                        p?.let {
                            selectedLocation = it
                            mapView.controller.animateTo(it)
                            mapView.controller.setZoom(5.0)
                            cityName = mapViewModel.getCityName(context, it.latitude, it.longitude).toString()
                            val mark = Marker(mapView).apply {
                                position = it
                                title = cityName
                                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            }
                            mapView.overlays.clear()
                            mapView.overlays.add(mark)
                            showBottomSheet.value = true
                        }
                        return true
                    }

                    override fun longPressHelper(p: GeoPoint?): Boolean {
                        return false
                    }
                }))
            }
        },
        modifier = Modifier.fillMaxSize()
    )

    selectedCity?.let{
        selectedLocation = it
        mapView.controller.animateTo(it)
        mapView.controller.setZoom(5.0)
        cityName = mapViewModel.getCityName(context, it.latitude, it.longitude).toString()
        val mark = Marker(mapView).apply {
            position = it
            title = cityName
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        }
        mapView.overlays.clear()
        mapView.overlays.add(mark)
        showBottomSheet.value = true
    }
    if (showBottomSheet.value) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet.value = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp , vertical = 50.dp),
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
                        if (isFav) {
                            mapViewModel.addCityToFav(
                                Favourites(
                                    city = cityName,
                                    lat = selectedLocation?.latitude ?: 0.0,
                                    lon = selectedLocation?.longitude ?: 0.0
                                )
                            )
                        } else {
                            showBottomSheet.value = false
                            selectedLocation?.let {
                                mapViewModel.saveLatLng(context, it.latitude, it.longitude)
                            }
                        }
                        navHostController.popBackStack()
                    }
                ) {
                    Text("OK")
                }
            }
        }
    }
}

@Composable
fun SearchBar(searchText: TextFieldValue,suggestions:List<SearchCity>, onSearch: (String) -> Unit , onCitySelected:(SearchCity)->Unit) {
    var textState by remember { mutableStateOf(searchText) }
    Column {
        OutlinedTextField(
            value = textState,
            onValueChange = {
                textState = it
                 onSearch(it.text)
            },
            placeholder = { Text("Search for a city...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.White),
            singleLine = true
        )

        if (suggestions.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(Color.White)
            ) {
                items(suggestions.size) {
                    Text(
                        text = suggestions.get(it).cityName,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onCitySelected(suggestions.get(it)) }
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}