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

    private var _url = MutableLiveData<String?>()
    val url: LiveData<String?>
        get() = _url

    init {
        val config = PagedList.Config.Builder()
            .setPageSize(10)
            .setEnablePlaceholders(false)
            .build()
        notices = initializedPagedListBuilder(config).build()
        _url.value = null
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

    fun showNotice(notice: Notice){
        _url.value = notice.url
    }

    fun endShowNotice(){
        _url.value = null
    }
}