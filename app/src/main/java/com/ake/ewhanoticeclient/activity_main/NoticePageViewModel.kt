package com.ake.ewhanoticeclient.activity_main

import androidx.lifecycle.*
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.ake.ewhanoticeclient.database.Board
import com.ake.ewhanoticeclient.network.Notice

class NoticePageViewModel(
    private val board: Board) : ViewModel() {

    var notices :LiveData<PagedList<Notice>>

    init {
        val config = PagedList.Config.Builder()
            .setPageSize(10)
            .setEnablePlaceholders(false)
            .build()
        notices = initializedPagedListBuilder(config).build()
    }

    private fun initializedPagedListBuilder(config: PagedList.Config):
            LivePagedListBuilder<Int, Notice>{
        val dataSourceFactory = object : DataSource.Factory<Int, Notice>(){
            override fun create(): DataSource<Int, Notice> {
                return NoticeDataSource(board.boardId, viewModelScope)
            }
        }
        return LivePagedListBuilder<Int, Notice>(dataSourceFactory, config)
    }
}