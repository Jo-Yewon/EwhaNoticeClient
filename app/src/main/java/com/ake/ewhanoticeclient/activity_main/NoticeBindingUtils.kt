package com.ake.ewhanoticeclient.activity_main

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.ake.ewhanoticeclient.network.Notice
import java.lang.StringBuilder

@BindingAdapter("noticeTitle")
fun TextView.setNoticeTitle(item: Notice){
    item?.let {
        val title = StringBuilder()
        if (item.category != null)
            title.append("[${item.category}]")
        title.append(item.title)
        text = title
    }
}
