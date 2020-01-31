package com.ake.ewhanoticeclient.activity_subscribe

import android.view.View
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

    val isSubscribedBoard = Transformations.map(subscribedBoards){
        if (it.isEmpty()) View.GONE else View.VISIBLE }

    init {
        _subscribedBoards.value = listOf()
        initBoards()
    }

    private fun initBoards() {
        uiScope.launch {
            _subscribedBoards.value = repository.getSubscribedBoardList()
            _unsubscribedBoards.value = repository.getBoardsFromDatabase()
        }
    }

    fun unsubscribeBoard(board: Board) {
        var subscribedBoards = (_subscribedBoards.value as MutableList).toMutableList()
        for (i in 0 until subscribedBoards.size)
            if (subscribedBoards[i].boardId == board.boardId) {
                subscribedBoards.removeAt(i)
                break
            }
        _subscribedBoards.value = subscribedBoards
    }

    fun subscribeBoard(board: Board) {
        _subscribedBoards.value =
            if (_subscribedBoards.value?.size == 0)
                mutableListOf(board)
            else {
                val subscribedBoards = (_subscribedBoards.value as MutableList).toMutableList()
                for (existedBoard in subscribedBoards)
                    if (existedBoard.boardId == board.boardId) {
                        //TODO 이미 구독된 게시판이라고 알려주어야 해요
                        return
                    }
                subscribedBoards.add(board)
                subscribedBoards
            }
    }

    fun clickConfirm() {
        //TODO
    }
}