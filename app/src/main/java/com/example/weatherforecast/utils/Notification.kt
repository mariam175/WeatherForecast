package com.example.weatherforecast.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.weatherforecast.R

class Notification {
     companion object{
         fun showNotification(weatherInfo: String ,  context: Context) {
             val channelId = "weather_alert_channel"
             val notificationManager =
                 context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                 val channel = NotificationChannel(channelId, "Weather Alerts", NotificationManager.IMPORTANCE_HIGH)
                 notificationManager.createNotificationChannel(channel)
             }

             val notification = NotificationCompat.Builder(context, channelId)
                 .setContentTitle("Weather Alert")
                 .setContentText(weatherInfo)
                 .setSmallIcon(R.drawable.weather_icon)
                 .build()

             notificationManager.notify(1, notification)
         }
     }
}