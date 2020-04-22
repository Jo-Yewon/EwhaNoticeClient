package com.ake.ewhanoticeclient.activity_subscribe

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ake.ewhanoticeclient.R
import com.ake.ewhanoticeclient.activity_main.MainActivity
import com.ake.ewhanoticeclient.database.BoardDatabase
import com.ake.ewhanoticeclient.repositories.BoardRepository
import com.ake.ewhanoticeclient.databinding.ActivitySubscribeBinding

class SubscribeActivity : AppCompatActivity() {

    lateinit var binding: ActivitySubscribeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_subscribe)

        val dao = BoardDatabase.getInstance(application).boardDatabaseDao
        val sharedPreferences = getSharedPreferences(
            BoardRepository.PREFERENCES_NAME, Context.MODE_PRIVATE)
        val repository = BoardRepository.getInstance(dao, sharedPreferences)
        val factory = SubscribeViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, factory)
            .get(SubscribeViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // SubscribedBoardList
        val layoutManager = LinearLayoutManager(this).apply{
            orientation = LinearLayoutManager.HORIZONTAL
        }
        val subscribedBoardsListAdapter = SubscribedBoardsAdapter(
            BoardClickListener { board ->
                viewModel.unsubscribeBoard(board)
            })
        binding.subscribedBoardList.layoutManager = layoutManager
        binding.subscribedBoardList.adapter = subscribedBoardsListAdapter

        // BoardList
        val bottomBoardsListAdapter = BottomBoardsAdapter(
            BoardClickListener { board ->
                viewModel.subscribeBoard(board)
            })
        binding.unsubscribedBoardList.adapter = bottomBoardsListAdapter

        //Search
        binding.searchViewBoard.apply {
            setOnQueryTextListener(BoardOnQueryTextListener(viewModel))
            setOnCloseListener {
                viewModel.resetSearch()
                true
            }
        }

        // Navigate
        viewModel.navigateToMainActivity.observe(this, Observer {
            if (it){ startActivity(Intent(this, MainActivity::class.java)) }
        })
    }

    class BoardOnQueryTextListener(private val viewModel: SubscribeViewModel) :
        SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(p0: String?): Boolean {
            viewModel.searchBoardByKeyword(p0)
            return true
        }

        override fun onQueryTextChange(p0: String?): Boolean {
            if (p0 == "") viewModel.resetSearch()
            return true
        }
    }
}
