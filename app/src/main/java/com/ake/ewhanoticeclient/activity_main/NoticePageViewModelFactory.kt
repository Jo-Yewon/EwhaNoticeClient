package com.ake.ewhanoticeclient.activity_main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ake.ewhanoticeclient.database.Board
import com.ake.ewhanoticeclient.network.ServerApi

class NoticePageViewModelFactory(private val board: Board,
                                 private val apiService: ServerApi) :
    ViewModelProvider.Factory {
    @Suppress("unchecked-cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoticePageViewModel::class.java)) {
            return NoticePageViewModel(board, apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
