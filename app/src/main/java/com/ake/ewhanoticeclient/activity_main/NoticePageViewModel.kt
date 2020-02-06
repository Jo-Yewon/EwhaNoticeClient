package com.ake.ewhanoticeclient.activity_main

import android.view.View
import androidx.lifecycle.*
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.ake.ewhanoticeclient.database.Board
import com.ake.ewhanoticeclient.network.Notice

class NoticePageViewModel(
    private val board: Board
) : ViewModel() {

    var notices: LiveData<PagedList<Notice>>

    private var _url = MutableLiveData<String?>()
    val url: LiveData<String?>
        get() = _url

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private var _initialLoading = MutableLiveData<Boolean>()
    val isInitialLoading = Transformations.map(_initialLoading) {
        if (it) View.VISIBLE
        else View.INVISIBLE
    }

    private var _isError = MutableLiveData<Boolean>()
    val isError = Transformations.map(_isError) {
        if (it) View.VISIBLE
        else View.INVISIBLE
    }

    private lateinit var dataSource: DataSource<Int, Notice>
    private val config by lazy {
        PagedList.Config.Builder()
            .setPageSize(10)
            .setEnablePlaceholders(false)
            .build()
    }

    init {
        notices = initializedPagedListBuilder(config).build()
        _url.value = null
        _isLoading.value = false
        _initialLoading.value = true
        _isError.value = false
    }

    private fun initializedPagedListBuilder(config: PagedList.Config):
            LivePagedListBuilder<Int, Notice> {
        val dataSourceFactory = object : DataSource.Factory<Int, Notice>() {
            override fun create(): DataSource<Int, Notice> {
                dataSource =
                    NoticeDataSource(board.boardId, viewModelScope, this@NoticePageViewModel)
                return dataSource
            }
        }
        startLoad()
        return LivePagedListBuilder<Int, Notice>(dataSourceFactory, config)
            .setBoundaryCallback(object : PagedList.BoundaryCallback<Notice?>() {
                override fun onItemAtFrontLoaded(itemAtFront: Notice) {
                    super.onItemAtFrontLoaded(itemAtFront)
                    endLoad()
                }
            })
    }

    fun showNotice(notice: Notice) {
        _url.value = notice.url
    }

    fun endShowNotice() {
        _url.value = null
    }

    fun refreshNotice() {
        notices = initializedPagedListBuilder(config).build()
        endRefresh()
        endLoad()
    }

    private fun endRefresh() {
        _isLoading.value = false
    }

    private fun startLoad() {
        _initialLoading.value = true
    }

    fun endLoad() {
        _initialLoading.value = false
    }

    fun showError() {
        _isError.value = true
    }
}