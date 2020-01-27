package com.ake.ewhanoticeclient.activity_subscribe
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ake.ewhanoticeclient.database.Board
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SubscribeViewModel(private val repository: BoardRepository): ViewModel(){

    private var _subscribedBoards = MutableLiveData<List<Board>>()
    val subscribedBoards: LiveData<List<Board>>
        get() = _subscribedBoards

    private var _unsubscribedBoards = MutableLiveData<List<Board>>()
    val unsubscribedBoards: LiveData<List<Board>>
        get() = _unsubscribedBoards

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init{
        initBoards()
    }

    private fun initBoards(){
        uiScope.launch {
            _subscribedBoards.value = repository.getSubscribedBoardList()
            _unsubscribedBoards.value = repository.getUnsubscribedBoardsFromDatabase()
        }
    }

    fun unsubscribeBoard(boardId: Int){
        //TODO
    }

    fun subscribeBoard(boardId: Int){
        //TODO
    }

    fun clickConfirm(){
        //TODO
    }
}