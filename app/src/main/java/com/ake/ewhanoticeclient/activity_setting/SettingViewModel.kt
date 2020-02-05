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
        _navigate.value = null
        _isPushAlarm.value = boardRepository.getPushStatus()
    }

    fun backButtonClicked(){ _navigate.value = BACK }

    fun subscribeButtonClicked(){ _navigate.value = SUBSCRIBE }

    fun githubButtonClicked(){ _navigate.value = GITHUB }

    fun reportButtonClicked(){ _navigate.value = REPORT }

    fun endNavigate(){ _navigate.value = null }

    fun togglePushAlarm(){
        _isPushAlarm.value?.let {
            boardRepository.setPushStatus(!it)
            _isPushAlarm.value = !it
        }
    }

    companion object{
        // Navigation flag
        const val BACK = 0
        const val GITHUB = 1
        const val REPORT = 2
        const val SUBSCRIBE = 3
    }
}
