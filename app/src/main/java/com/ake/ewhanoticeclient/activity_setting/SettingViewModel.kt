package com.ake.ewhanoticeclient.activity_setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ake.ewhanoticeclient.database.BoardRepository

class SettingViewModel(private val boardRepository: BoardRepository): ViewModel(){

    private val _isPushAlarm = MutableLiveData<Boolean>()
    val isPushAlarm: LiveData<Boolean>
    get() = _isPushAlarm

    private val _navigate = MutableLiveData<Int>()
    val navigate: LiveData<Int>
    get() = _navigate

    init {
        _navigate.value = DEFAULT
        _isPushAlarm.value = boardRepository.getPushStatus()
    }

    fun backButtonClicked(){ _navigate.value = BACK }

    fun subscribeButtonClicked(){ _navigate.value = SUBSCRIBE  }

    fun infoButtonClicked(){_navigate.value = INFO}

    fun endNavigate(){ _navigate.value = DEFAULT }

    fun togglePushAlarm(){
        _isPushAlarm.value?.let {
            boardRepository.setPushStatus(!it)
            _isPushAlarm.value = !it
        }
    }

    companion object{
        // Navigation flag
        const val DEFAULT = 0
        const val BACK = 1
        const val INFO = 2
        const val SUBSCRIBE = 3
    }
}
