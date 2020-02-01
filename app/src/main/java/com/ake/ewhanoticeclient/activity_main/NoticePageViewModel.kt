package com.ake.ewhanoticeclient.activity_main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ake.ewhanoticeclient.database.Board

class NoticePageViewModel(
    private val board: Board) : ViewModel() {

    private val _index = MutableLiveData<Int>()
    val text: LiveData<String> = Transformations.map(_index) {
        "Hello world from section: $it"
    }

    init {
        
    }

    fun setIndex(index: Int) {
        _index.value = index
    }
}