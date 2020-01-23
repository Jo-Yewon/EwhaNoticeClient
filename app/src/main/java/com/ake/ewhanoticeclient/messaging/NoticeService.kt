package com.ake.ewhanoticeclient.messaging

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ake.ewhanoticeclient.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class NoticeService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        if (message != null)
            sendNotification(message)
    }

    private fun sendNotification(message: RemoteMessage) {
        val builder =
            NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setContentTitle("테스트알림")
                .setContentText("테스트내용")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)){
            notify(1, builder.build())
        }
    }
}
