package com.ake.ewhanoticeclient.messaging

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.text.Html
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ake.ewhanoticeclient.R
import com.ake.ewhanoticeclient.activity_main.MainActivity
import com.ake.ewhanoticeclient.database.BoardRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*

class NoticeService : FirebaseMessagingService() {
    class SimpleNotice(val boardId: Int, val title: String) {
        companion object {
            fun getSimpleNoticeFromString(string: String): SimpleNotice? {
                for ((index, value) in string.withIndex())
                    if (value == '&') {
                        return SimpleNotice(
                            string.substring(0, index).toInt(),
                            string.substring(index + 1)
                        )
                    }
                return null
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val sharedPreferences =
            getSharedPreferences(BoardRepository.PREFERENCES_NAME, Context.MODE_PRIVATE)
        val boardRepository = BoardRepository(null, sharedPreferences)

        if (boardRepository.getPushStatus()) {
            val notices = message.data
            val subscribedBoards = boardRepository.getSubscribedBoardList()

            var index = 0

            while (true) {
                val noticeString = notices[index.toString()]
                noticeString ?: break  // End of notices

                SimpleNotice.getSimpleNoticeFromString(noticeString)?.let {
                    for (subscribedBoard in subscribedBoards)
                        if (subscribedBoard.boardId == it.boardId) {
                            sendNotification(it)
                            return
                        }
                }
                index += 1
            }
        }
    }

    private fun sendNotification(notice: SimpleNotice) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder =
            NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setContentTitle(
                    Html.fromHtml(
                        String.format(
                            Locale.getDefault(),
                            "<strong>%s</strong>",
                            "새로운 공지사항"
                        )
                    )
                )
                .setContentText("\"${notice.title}\"")
                .setStyle(
                    NotificationCompat.BigTextStyle().bigText(
                        "\"${notice.title}\" 외 여러 건의 새로운 공지사항이 있습니다."
                    )
                )
                .setSmallIcon(R.drawable.notification_icon)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(1, builder.build())
        }
    }
}
