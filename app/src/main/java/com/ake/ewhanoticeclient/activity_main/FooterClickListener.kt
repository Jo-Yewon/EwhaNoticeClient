package com.ake.ewhanoticeclient.activity_main

class FooterClickListener(val clickListener: (Unit) -> Unit){
    fun onClick() = clickListener(Unit)
}