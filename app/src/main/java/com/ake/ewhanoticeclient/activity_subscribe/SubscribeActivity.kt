package com.ake.ewhanoticeclient.activity_subscribe

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ake.ewhanoticeclient.R
import com.ake.ewhanoticeclient.activity_main.MainActivity
import com.ake.ewhanoticeclient.database.BoardDatabase
import com.ake.ewhanoticeclient.databinding.ActivitySubscribeBinding

class SubscribeActivity : AppCompatActivity() {

    lateinit var binding: ActivitySubscribeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_subscribe)

        val dao = BoardDatabase.getInstance(application).BoardDatabaseDao
        val sharedPreferences = getSharedPreferences(
            BoardRepository.PREFERENCES_NAME, Context.MODE_PRIVATE)
        val repository = BoardRepository.getInstance(dao, sharedPreferences)
        val factory = SubscribeViewModelFactory(repository)
        var viewModel = ViewModelProviders.of(this, factory)
            .get(SubscribeViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // SubscribedBoardList
        val subscribedBoardsListAdapter = SubscribedBoardAdapter(
            BoardClickListener { board ->
                viewModel.unsubscribeBoard(board)
            })
        binding.subscribedBoardList.adapter = subscribedBoardsListAdapter
        viewModel.subscribedBoards.observe(this, Observer {
            it?.let { subscribedBoardsListAdapter.submitList(it) }
        })

        //BoardList
        val unsubscribedBoardsListAdapter = BoardAdapter(
            BoardClickListener { board ->
                viewModel.subscribeBoard(board)
            })
        binding.unsubscribedBoardList.adapter = unsubscribedBoardsListAdapter
        viewModel.bottomBoards.observe(this, Observer {
            it?.let { unsubscribedBoardsListAdapter.submitList(it) }
        })

        //Search
        binding.searchViewBoard.apply {
            setOnQueryTextListener(BoardOnQueryTextListener(viewModel))
            setOnCloseListener {
                viewModel.closeSearch()
                true
            }
        }

        //Navigate
        viewModel.navigateToMainActivity.observe(this, Observer {
            if (it) startActivity(Intent(this, MainActivity::class.java))
        })
    }

    class BoardOnQueryTextListener(private val viewModel: SubscribeViewModel) :
        SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(p0: String?): Boolean {
            viewModel.searchBoardByKeyword(p0)
            return true
        }

        override fun onQueryTextChange(p0: String?): Boolean {
            if (p0 == "") viewModel.closeSearch()
            return true
        }
    }
}
