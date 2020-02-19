package com.ake.ewhanoticeclient.activity_main

class HeaderFooterClickListener(val clickListener: (Unit) -> Unit){
    fun onClick() = clickListener(Unit)
}