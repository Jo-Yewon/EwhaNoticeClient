package com.ake.ewhanoticeclient.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.ake.ewhanoticeclient.R
import com.ake.ewhanoticeclient.adapter.BoardAdapter
import com.ake.ewhanoticeclient.adapter.SelectedBoardAdapter
import com.ake.ewhanoticeclient.database.Board
import com.ake.ewhanoticeclient.database.BoardDatabase
import com.ake.ewhanoticeclient.databinding.ActivitySubscribeBinding
import com.ake.ewhanoticeclient.subscription.SubscriptionManager
import kotlinx.coroutines.*

class SubscribeActivity : AppCompatActivity() {

    lateinit var binding: ActivitySubscribeBinding
    val dataSource by lazy { BoardDatabase.getInstance(application).BoardDatabaseDao }

    private var job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    lateinit var selectedBoardAdapter: SelectedBoardAdapter
    lateinit var subscribedBoardList: MutableList<Board>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_subscribe)

        selectedBoardAdapter = loadSubscribedBoards()
        loadBoards()
    }

    private fun loadSubscribedBoards(): SelectedBoardAdapter{
        subscribedBoardList = SubscriptionManager.getSubscribedBoardList(this)
        val selectedBoardAdapter = SelectedBoardAdapter(subscribedBoardList)

        binding.selectedBoardList.apply{
            layoutManager = LinearLayoutManager(this@SubscribeActivity)
            adapter = selectedBoardAdapter
        }
        return selectedBoardAdapter
    }

    private fun loadBoards(){
        binding.boardList.layoutManager = LinearLayoutManager(this)
        uiScope.launch {
            startLoad()

            val boards = getBoardsFromDatabase()
            boards?.filter {
                for (subscribeBoard in subscribedBoardList)
                    if (subscribeBoard.boardId == it.boardId)
                        false
                true
            }

            val adapter = when(boards){
                null -> BoardAdapter(mutableListOf())
                else -> BoardAdapter(boards)
            }
            adapter.seletedBoardAdapter = selectedBoardAdapter
            binding.boardList.adapter = adapter

            selectedBoardAdapter.boardAdapter = adapter
            endLoad()
        }
    }

    private suspend fun getBoardsFromDatabase(): MutableList<Board>?{
        return withContext(Dispatchers.IO){
            dataSource.getAllBoards()
        }
    }

    private fun startLoad(){
        //TODO()
    }

    private fun endLoad(){
        //TODO()
    }
}
