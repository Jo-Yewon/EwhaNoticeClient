package com.ake.ewhanoticeclient.activity_main

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.ake.ewhanoticeclient.network.Notice
import java.lang.StringBuilder

@BindingAdapter("noticeTitle")
fun TextView.setNoticeTitle(item: Notice){
    item?.let {
        val stringBuilder = StringBuilder()

        if (item.category != null)
            if (item.category[0]=='[') stringBuilder.append("${item.category} ")
            else stringBuilder.append("[${item.category}] ")

        var title = item.title
        if (title.endsWith('\n')) title = title.substring(0, title.length-1)
        if (title[0]=='\n') stringBuilder.append(title.substring(1))
        else stringBuilder.append(title)

        text = stringBuilder.toString()
    }
}

@BindingAdapter("date")
fun TextView.setDate(item:Notice){
    item?.let{
        text = item.date.substring(2)
    }
}
