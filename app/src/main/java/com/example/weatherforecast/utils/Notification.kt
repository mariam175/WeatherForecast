package com.example.weatherforecast.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent

import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager

import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.weatherforecast.MainActivity
import com.example.weatherforecast.R

class Notification {
     companion object {
         fun showNotification(weatherInfo: String, context: Context) {
             val channelId = "weather_alert_channel"
             val notificationManager =
                 context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                 val channel = NotificationChannel(
                     channelId,
                     "Weather Alerts",
                     NotificationManager.IMPORTANCE_HIGH
                 )
                 notificationManager.createNotificationChannel(channel)
             }

             val intent = Intent(context, MainActivity::class.java).apply {
                 flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
             }
             val pendingIntent = PendingIntent.getActivity(
                 context,
                 0,
                 intent,
                 PendingIntent.FLAG_IMMUTABLE
             )

             val notification = NotificationCompat.Builder(context, channelId)
                 .setContentTitle("Weather Alert")
                 .setContentText(weatherInfo)
                 .setContentIntent(pendingIntent)
                 .setSmallIcon(R.drawable.weather_icon)
                 .build()

             notificationManager.notify(1, notification)
         }

         fun showNotificationLong(weatherInfo: String, context: Context) {
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                 val channel = NotificationChannel(
                     "alarm_channel_id",
                     "Alarm Channel",
                     NotificationManager.IMPORTANCE_HIGH
                 ).apply {
                     description = "Used for weather alarms"
                     enableLights(true)
                     enableVibration(true)
                     setSound(
                         RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM),
                         AudioAttributes.Builder()
                             .setUsage(AudioAttributes.USAGE_ALARM)
                             .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                             .build()
                     )

                 }

                 val manager = context.getSystemService(NotificationManager::class.java)
                 manager.createNotificationChannel(channel)
             }

             val intent = Intent(context, MainActivity::class.java).apply {
                 flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
             }
             val pendingIntent = PendingIntent.getActivity(
                 context,
                 0,
                 intent,
                 PendingIntent.FLAG_IMMUTABLE
             )

             val notification = NotificationCompat.Builder(context, "alarm_channel_id")
                 .setSmallIcon(R.drawable.weather_icon)
                 .setContentTitle("Weather Alert")
                 .setContentText(weatherInfo)
                 .setPriority(NotificationCompat.PRIORITY_MAX)
                 .setCategory(NotificationCompat.CATEGORY_ALARM)
                 .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                 .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                 .setAutoCancel(false)
                 .setOngoing(true)

                 .setFullScreenIntent(pendingIntent, true)
                 .build()

             val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
             manager.notify(1001, notification)
         }
     }
}