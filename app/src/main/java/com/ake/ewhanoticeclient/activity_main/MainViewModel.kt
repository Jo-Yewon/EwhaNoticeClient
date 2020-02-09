package com.ake.ewhanoticeclient.activity_main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel(){
    private var _navigateToSetting = MutableLiveData<Boolean>()
    val navigateToSetting: LiveData<Boolean>
        get() = _navigateToSetting

    fun clickSetting(){
        _navigateToSetting.value = true
    }

    fun endNavigateToSetting(){
        _navigateToSetting.value
    }
}