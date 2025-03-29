package com.example.weatherforecast.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

class CheckNetwork{
    companion object{
        fun checkNetwork(context: Context) : Boolean{
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val isConnected = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = connectivityManager.activeNetwork
                val capabilities = connectivityManager.getNetworkCapabilities(network)
                capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
            } else {
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                activeNetworkInfo?.isConnected == true
            }
            return isConnected
        }
    }
}