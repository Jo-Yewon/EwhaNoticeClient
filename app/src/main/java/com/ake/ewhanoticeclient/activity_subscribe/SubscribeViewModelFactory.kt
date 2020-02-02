package com.ake.ewhanoticeclient.activity_subscribe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ake.ewhanoticeclient.database.BoardRepository

class SubscribeViewModelFactory(
    private val subscribeRepository: BoardRepository
): ViewModelProvider.Factory{
    @Suppress("unchecked-cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SubscribeViewModel::class.java)){
            return SubscribeViewModel(subscribeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
