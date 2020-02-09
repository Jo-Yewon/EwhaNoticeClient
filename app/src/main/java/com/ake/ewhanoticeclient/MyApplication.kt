package com.ake.ewhanoticeclient

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // 오레오 이상 버전에서는 알림 채널 생성
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(
                    getString(R.string.default_notification_channel_id),
                    getString(R.string.default_notification_channel_id),
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            notificationChannel.apply {
                description = getString(R.string.notification_description)
                enableLights(true)
                enableVibration(true)
                setShowBadge(false)
                vibrationPattern = longArrayOf(100, 200, 100, 200)
            }

            val notificationManage =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManage.createNotificationChannel(notificationChannel)
        }
    }
}