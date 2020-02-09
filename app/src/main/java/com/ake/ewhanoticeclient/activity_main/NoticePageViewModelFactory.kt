package com.ake.ewhanoticeclient.activity_main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ake.ewhanoticeclient.activity_subscribe.SubscribeViewModel
import com.ake.ewhanoticeclient.database.Board
import com.ake.ewhanoticeclient.database.BoardRepository

class NoticePageViewModelFactory(private val board: Board): ViewModelProvider.Factory{
    @Suppress("unchecked-cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoticePageViewModel::class.java)){
            return NoticePageViewModel(board) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
