package com.ake.ewhanoticeclient.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.ake.ewhanoticeclient.R
import com.ake.ewhanoticeclient.adapter.BoardAdapter
import com.ake.ewhanoticeclient.database.Board
import com.ake.ewhanoticeclient.database.BoardDatabase
import com.ake.ewhanoticeclient.databinding.ActivityWelcomeBinding
import kotlinx.coroutines.*

class WelcomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityWelcomeBinding
    val dataSource by lazy { BoardDatabase.getInstance(application).BoardDatabaseDao }

    private var job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome)

        binding.boardList.layoutManager = LinearLayoutManager(this)
        loadBoards()
    }

    private fun loadBoards(){
        uiScope.launch {
            startLoad()

            val boards = getBoardsFromDatabase()
            if (boards != null)
                binding.boardList.adapter = BoardAdapter(boards)

            endLoad()
        }
    }

    private suspend fun getBoardsFromDatabase(): List<Board>?{
        return withContext(Dispatchers.IO){
            dataSource.getAllBoards()
        }
    }

    private fun startLoad(){

    }

    private fun endLoad(){

    }
}
