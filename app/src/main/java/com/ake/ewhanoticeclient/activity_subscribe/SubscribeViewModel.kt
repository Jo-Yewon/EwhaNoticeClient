package com.ake.ewhanoticeclient.activity_subscribe

import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.ake.ewhanoticeclient.database.asDomainModel
import com.ake.ewhanoticeclient.domain.Board
import com.ake.ewhanoticeclient.repositories.BoardRepository
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch

class SubscribeViewModel(private val repository: BoardRepository) : ViewModel() {

    private var _subscribedBoards = MutableLiveData<List<Board>>()
    val subscribedBoards: LiveData<List<Board>>
        get() = _subscribedBoards

    private var _bottomBoards = MutableLiveData<List<Board>>()
    val bottomBoards: LiveData<List<Board>>
        get() = _bottomBoards

    val isSubscribedBoard = Transformations.map(subscribedBoards) {
        if (it.isEmpty()) View.GONE else View.VISIBLE
    }

    private val _navigateToMainActivity = MutableLiveData<Boolean>()
    val navigateToMainActivity: LiveData<Boolean>
        get() = _navigateToMainActivity

    private lateinit var allBoards: List<Board>

    init {
        _navigateToMainActivity.value = false
        initBoards()
    }

    private fun initBoards() {
        viewModelScope.launch {
            _subscribedBoards.value = repository.getSubscribedBoardList()
            // TODO dispatch
            allBoards = repository.getBoardsFromDatabase().asDomainModel()
            _bottomBoards.value = allBoards
        }
    }

    fun unsubscribeBoard(board: Board) {
        val subscribedBoards = (_subscribedBoards.value as MutableList).toMutableList()
        for (i in 0 until subscribedBoards.size)
            if (subscribedBoards[i].boardId == board.boardId) {
                subscribedBoards.removeAt(i)
                break
            }
        _subscribedBoards.value = subscribedBoards
    }

    fun subscribeBoard(board: Board) {
        when (_subscribedBoards.value?.size) {
            0 -> _subscribedBoards.value = mutableListOf(board)
            5 -> {
                //TODO 5개 이상 구독할 수 없다고 알려주어야 해요
                Log.d("subscribe", "wait")
            }
            else -> {
                val subscribedBoards = (_subscribedBoards.value as MutableList).toMutableList()
                for (existedBoard in subscribedBoards)
                    if (existedBoard.boardId == board.boardId) {
                        //TODO 이미 구독된 게시판이라고 알려주어야 해요
                        return
                    }
                subscribedBoards.add(board)
                _subscribedBoards.value = subscribedBoards
            }
        }
    }

    fun searchBoardByKeyword(keyword: String?) {
        viewModelScope.launch {
            when (keyword) {
                null -> resetSearch()
                else -> _bottomBoards.value = repository.searchBoardsFromDatabase(keyword).asDomainModel()
            }
        }
    }

    fun resetSearch() {
        _bottomBoards.value = allBoards
    }

    fun clickConfirm() {
        subscribeBoards()
        _navigateToMainActivity.value = true
    }

    private fun subscribeBoards() {
        val firebaseMessaging = FirebaseMessaging.getInstance()
        viewModelScope.launch {
            //TODO dispatch
            for (topic in repository.getAllTopics())
                firebaseMessaging.unsubscribeFromTopic(topic)
            for (board in _subscribedBoards.value!!)
                firebaseMessaging.subscribeToTopic(board.boardCategory)
        }
        repository.setSubscribedBoardList(_subscribedBoards.value!!)
    }
}