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
            if (item.category[0]=='[') title.append("${item.category} ")
            else title.append("[${item.category}] ")

        if (item.title[0]=='\n') title.append(item.title.substring(1))
        else title.append(item.title)

        text = title.toString()
    }
}
