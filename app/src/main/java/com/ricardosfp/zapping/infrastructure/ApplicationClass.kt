package com.ricardosfp.zapping.infrastructure

import android.app.*
import android.os.Build
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@HiltAndroidApp
class ApplicationClass: Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        // create notification channels for Android Oreo (26) and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            for (notificationChannel in NotificationChannel.values()) {
                val channel = NotificationChannel(
                    notificationChannel.id,
                    notificationChannel.getName(this),
                    notificationChannel.importance)
                channel.description = notificationChannel.getDescription(this)
                val notificationManager = getSystemService(NotificationManager::class.java)
                notificationManager.createNotificationChannel(channel)
            }
        }
    }
}