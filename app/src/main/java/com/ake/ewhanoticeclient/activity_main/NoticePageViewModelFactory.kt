package com.ake.ewhanoticeclient.activity_main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ake.ewhanoticeclient.domain.Board

class NoticePageViewModelFactory(private val board: Board,
                                 private val application: Application) :
    ViewModelProvider.Factory {
    @Suppress("unchecked-cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoticePageViewModel::class.java)) {
            return NoticePageViewModel(board, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
