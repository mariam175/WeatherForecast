package com.example.weatherforecast.map

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay

@Composable
fun MapScreenOSM(navHostController: NavHostController,
                 mapViewModel: MapViewModel,
                 context: Context,
                 isFav:Boolean){
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
  var selectedLocation = remember { mutableStateOf<GeoPoint?>(null) }
    var cityName = remember { mutableStateOf<String>("") }
    AndroidView(
        factory = { ctx ->
            mapView.apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                controller.setZoom(12.0)
                controller.setCenter(GeoPoint(51.5074, -0.1278))
//                overlays.add(object : MapEventsOverlay(object : MapEventsReceiver {
//                    override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
//                        p?.let {
//                            selectedLocation.value = it
//                            getCityNameFromLatLng(it.latitude, it.longitude) { name ->
//                                cityName = name
//                            }
//                        }
//                        return true
//                    }
//
//                    override fun longPressHelper(p: GeoPoint?): Boolean {
//                        TODO("Not yet implemented")
//                    }
//
//                }))
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}