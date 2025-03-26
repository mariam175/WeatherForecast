package com.example.weatherforecast.map

import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.ViewModel
import com.example.weatherforecast.utils.SettingsChanges
import java.util.Locale

class MapViewModel :ViewModel() {
     fun getCityName(context: Context , lat: Double,long: Double):String? {
        var cityName: String?
        val geoCoder = Geocoder(context, Locale.getDefault())
        val address = geoCoder.getFromLocation(lat, long, 1)
        cityName = address?.get(0)?.adminArea
        if (cityName == null) {
            cityName = address?.get(0)?.locality
            if (cityName == null) {
                cityName = address?.get(0)?.subAdminArea
            }
        }
        return cityName
    }
    fun saveLatLng(context: Context, lat: Double , long: Double){
        SettingsChanges.saveLocation(context , lat , long)
    }
}