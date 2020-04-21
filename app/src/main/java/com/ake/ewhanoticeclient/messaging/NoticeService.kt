package com.ake.ewhanoticeclient.messaging

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.text.Html
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ake.ewhanoticeclient.R
import com.ake.ewhanoticeclient.activity_main.MainActivity
import com.ake.ewhanoticeclient.database.BoardDatabase
import com.ake.ewhanoticeclient.domain.SimpleNotice
import com.ake.ewhanoticeclient.repositories.BoardRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*


class NoticeService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        val sharedPreferences =
            getSharedPreferences(BoardRepository.PREFERENCES_NAME, Context.MODE_PRIVATE)
        val dao = BoardDatabase.getInstance(application).boardDatabaseDao
        val boardRepository = BoardRepository(dao, sharedPreferences)

        if (Messaging(sharedPreferences).getPushStatus()) {  // Check push-on
            try {
                // Check subscribed notice
                val notices = message.data
                val subscribedBoardsId = boardRepository.getSubscribedBoardList().map { it.boardId }

                var index = 0
                val subscribedNotices = mutableListOf<SimpleNotice>()
                while (true) {
                    val noticeString = notices[index.toString()]
                    noticeString ?: break  // End of notices

                    SimpleNotice.getSimpleNoticeFromString(noticeString)?.let {
                        if (it.boardId in subscribedBoardsId)
                            subscribedNotices.add(it)
                    }
                    index += 1
                }

                if (subscribedNotices.isNotEmpty())
                    sendNotification(subscribedNotices)

            } catch (e: Exception) {
                Log.d("messaging", e.message ?: "undefined")
            }
        }
    }

    private fun sendNotification(notices: List<SimpleNotice>) {
        val notificationIntent = Intent(applicationContext, MainActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        val pendingIntent =
            PendingIntent.getActivity(
                this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

        val builder =
            NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setContentTitle(
                    Html.fromHtml(
                        String.format(
                            Locale.getDefault(),
                            "<strong>%s</strong>",
                            "[이화여자대학교] 새로운 공지사항이 있습니다."
                        )
                    )
                )
                .setStyle(NotificationCompat.InboxStyle().also {
                    for (notice in notices)
                        it.addLine(notice.title)
                })
                .setSmallIcon(R.drawable.notification_icon)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(notices[0].boardId, builder.build())
        }
    }
}