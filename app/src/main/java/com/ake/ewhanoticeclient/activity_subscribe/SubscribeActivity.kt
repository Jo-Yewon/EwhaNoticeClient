package com.ake.ewhanoticeclient.activity_subscribe

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ake.ewhanoticeclient.R
import com.ake.ewhanoticeclient.database.BoardDatabase
import com.ake.ewhanoticeclient.databinding.ActivitySubscribeBinding

class SubscribeActivity : AppCompatActivity() {

    lateinit var binding: ActivitySubscribeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_subscribe)

        val dao = BoardDatabase.getInstance(application).BoardDatabaseDao
        val sharedPreferences = getSharedPreferences(
            BoardRepository.PREFERENCES_NAME, Context.MODE_PRIVATE
        )
        val repository = BoardRepository.getInstance(dao, sharedPreferences)
        val factory = SubscribeViewModelFactory(repository)
        var viewModel = ViewModelProviders.of(this, factory)
            .get(SubscribeViewModel::class.java)

        val subscribedBoardsListAdapter = BoardAdapter(
            BoardAdapter.BoardListener { boardId ->
                viewModel.unsubscribeBoard(boardId)
            })
        binding.subscribedBoardList.adapter = subscribedBoardsListAdapter
        viewModel.subscribedBoards.observe(this, Observer {
            it?.let { subscribedBoardsListAdapter.submitList(it) }
        })

        val unsubscribedBoardsListAdapter = BoardAdapter(
            BoardAdapter.BoardListener { boardId ->
                viewModel.subscribeBoard(boardId)
            })
        binding.unsubscribedBoardList.adapter = unsubscribedBoardsListAdapter
        viewModel.unsubscribedBoards.observe(this, Observer {
            it?.let { unsubscribedBoardsListAdapter.submitList(it) }
        })

        binding.lifecycleOwner = this
    }

    private fun startLoad() {
        //TODO()
    }

    private fun endLoad() {
        //TODO()
    }
}
