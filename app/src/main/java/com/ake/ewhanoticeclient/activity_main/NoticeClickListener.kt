package com.ake.ewhanoticeclient.activity_main

import com.ake.ewhanoticeclient.network.Notice

class NoticeClickListener(val clickListener: (notice: Notice) -> Unit){
    fun onClick(notice: Notice) = clickListener(notice)
}