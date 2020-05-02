package com.ake.ewhanoticeclient.activity_subscribe

import android.view.View
import androidx.lifecycle.*
import com.ake.ewhanoticeclient.database.asDomainModel
import com.ake.ewhanoticeclient.domain.Board
import com.ake.ewhanoticeclient.repositories.BoardRepository
import kotlinx.coroutines.launch

class SubscribeViewModel(
    private val boardRepository: BoardRepository) : ViewModel() {

    private val _subscribedBoards = MutableLiveData(boardRepository.getSubscribedBoardList())
    val subscribedBoards: LiveData<List<Board>>
        get() = _subscribedBoards

    private var _bottomBoards = MutableLiveData<List<Board>>()
    val bottomBoards: LiveData<List<Board>>
        get() = _bottomBoards

    val isSubscribedBoard = Transformations.map(subscribedBoards) {
        if (it.isEmpty()) View.GONE else View.VISIBLE
    }

    private val _endActivity = MutableLiveData<Boolean>()
    val endActivity: LiveData<Boolean>
        get() = _endActivity

    init {
        _endActivity.value = false
        resetSearch()
    }

    fun unsubscribeBoard(board: Board) {
        val subscribedBoards = _subscribedBoards.value ?: return
        if (board.boardId in subscribedBoards.map { it.boardId })
            _subscribedBoards.value = subscribedBoards.filter { it.boardId != board.boardId }
    }

    fun subscribeBoard(board: Board) {
        val subscribedBoards = _subscribedBoards.value ?: return
        if (subscribedBoards.size < 5 && board.boardId !in subscribedBoards.map { it.boardId })
            _subscribedBoards.value = subscribedBoards.toMutableList().apply {
                add(board)
            }
    }

    fun searchBoardByKeyword(keyword: String?) {
        viewModelScope.launch {
            when (keyword) {
                null -> resetSearch()
                else -> _bottomBoards.value = boardRepository.searchBoardsFromDatabase(keyword).asDomainModel()
            }
        }
    }

    fun resetSearch() {
        viewModelScope.launch {
            _bottomBoards.value = boardRepository.getBoardsFromDatabase().asDomainModel()
        }
    }

    fun confirmSubscribe() {
        val boards = _subscribedBoards.value ?: return
        boardRepository.subscribeBoards(boards)
        _endActivity.value = true
    }
}