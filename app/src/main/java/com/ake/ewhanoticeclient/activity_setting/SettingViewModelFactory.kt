package com.ake.ewhanoticeclient.activity_setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ake.ewhanoticeclient.messaging.Messaging

class SettingViewModelFactory(
    private val messaging: Messaging
): ViewModelProvider.Factory{
    @Suppress("unchecked-cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)){
            return SettingViewModel(messaging) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
