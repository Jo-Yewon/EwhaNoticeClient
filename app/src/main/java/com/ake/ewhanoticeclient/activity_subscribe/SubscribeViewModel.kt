package com.ake.ewhanoticeclient.activity_subscribe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ake.ewhanoticeclient.database.Board
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SubscribeViewModel(private val repository: BoardRepository) : ViewModel() {

    private var _subscribedBoards = MutableLiveData<List<Board>>()
    val subscribedBoards: LiveData<List<Board>>
        get() = _subscribedBoards

    private var _unsubscribedBoards = MutableLiveData<List<Board>>()
    val unsubscribedBoards: LiveData<List<Board>>
        get() = _unsubscribedBoards

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val descriptionVisible = Transformations.map(subscribedBoards) { it.isEmpty() }

    init {
        initBoards()
    }

    private fun initBoards() {
        uiScope.launch {
            _subscribedBoards.value = repository.getSubscribedBoardList()
            _unsubscribedBoards.value = repository.getUnsubscribedBoardsFromDatabase()
        }
    }

    fun unsubscribeBoard(board: Board) {
        var subscribedBoards = _subscribedBoards.value as MutableList
        subscribedBoards.remove(board)
        _subscribedBoards.value = subscribedBoards

        var unsubscribedBoards = _unsubscribedBoards.value as MutableList
        unsubscribedBoards.add(board)
        _unsubscribedBoards.value = unsubscribedBoards
    }

    fun subscribeBoard(board: Board) {
        var unsubscribedBoards = _unsubscribedBoards.value as MutableList
        unsubscribedBoards.remove(board)
        _unsubscribedBoards.value = unsubscribedBoards

        _subscribedBoards.value =
            if (_subscribedBoards.value?.size == 0)
                mutableListOf(board)
            else {
                val subscribedBoards = _subscribedBoards.value as MutableList
                subscribedBoards.add(board)
                subscribedBoards
            }
    }

    fun clickConfirm() {
        //TODO
    }
}