package com.ake.ewhanoticeclient.activity_main

import com.ake.ewhanoticeclient.domain.Notice

class NoticeClickListener(val clickListener: (notice: Notice) -> Unit){
    fun onClick(notice: Notice) = clickListener(notice)
}

class HeaderFooterClickListener(val clickListener: (Unit) -> Unit){
    fun onClick() = clickListener(Unit)
}