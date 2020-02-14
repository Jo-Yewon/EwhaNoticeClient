package com.ake.ewhanoticeclient.activity_main

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.ake.ewhanoticeclient.network.Notice
import java.text.SimpleDateFormat
import java.util.*

class NoticeBindingUtils {
    companion object {
        private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
        // 새로운 공지사항 판단 기준.
        const val NEW_STANDARD = 2

        fun getDateFromString(string: String): Date {
            return simpleDateFormat.parse(string)
        }
    }
}

@BindingAdapter("noticeTitle")
fun TextView.setNoticeTitle(item: Notice) {
    val stringBuilder = StringBuilder()

    if (item.category != null)
        if (item.category[0] == '[') stringBuilder.append("${item.category} ")
        else stringBuilder.append("[${item.category}] ")

    stringBuilder.append(item.title.replace("\n", ""))
    text = stringBuilder.toString()
}

@BindingAdapter("date")
fun TextView.setDate(item: Notice) {
    text = item.date.substring(2)
}

@BindingAdapter("isNewOne")
fun TextView.setIsNewOne(item: Notice) {
    val diffDate = Date().time - NoticeBindingUtils.getDateFromString(item.date).time
    visibility =
        if (diffDate / (60 * 60 * 24 * 1000) < NoticeBindingUtils.NEW_STANDARD) View.INVISIBLE
        else View.VISIBLE

}

@BindingAdapter("isNewOne")
fun ImageButton.setIsNewOne(item: Notice) {
    val diffDate = Date().time - NoticeBindingUtils.getDateFromString(item.date).time
    visibility =
        if (diffDate / (60 * 60 * 24 * 1000) < NoticeBindingUtils.NEW_STANDARD) View.VISIBLE
        else View.INVISIBLE

}