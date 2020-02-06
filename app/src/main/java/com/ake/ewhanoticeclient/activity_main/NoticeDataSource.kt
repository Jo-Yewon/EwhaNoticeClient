package com.ake.ewhanoticeclient.activity_main

import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.paging.PageKeyedDataSource
import com.ake.ewhanoticeclient.network.Notice
import com.ake.ewhanoticeclient.network.NoticeApi
import com.ake.ewhanoticeclient.network.Notices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class NoticeDataSource(
    private val boardId: Int,
    private val viewModelScope: CoroutineScope,
    private val viewModel: NoticePageViewModel
) : LifecycleObserver, PageKeyedDataSource<Int, Notice>() {

    private val apiService = NoticeApi.create()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Notice>
    ) {
        viewModelScope.launch {
            try {
                val response = apiService.getNoticesWithPage(boardId, 1)
                when (response.count) {
                    0 -> throw Exception("올바르지 않은 게시판에 접근함")
                    else -> callback.onResult(
                        response.results,
                        Notices.getPage(response.previous),
                        Notices.getPage(response.next)
                    )
                }
            } catch (exception: Exception) {
                Log.e("GetNoticeData", "Failed to fetch data")
                viewModel.endLoad()
                viewModel.showError()
            }
        }
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Notice>
    ) {
        viewModelScope.launch {
            try {
                val response = apiService.getNoticesWithPage(boardId, params.key)
                when (response.count) {
                    0 -> throw Exception("올바르지 않은 게시판에 접근함")
                    else -> callback.onResult(
                        response.results,
                        Notices.getPage(response.next)
                    )
                }
            } catch (exception: Exception) {
                Log.e("GetNoticeData", "Failed to fetch data")
                viewModel.endLoad()
                viewModel.showError()
            }
        }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Notice>
    ) {
        viewModelScope.launch {
            try {
                val response = apiService.getNoticesWithPage(boardId, params.key)
                when (response.count) {
                    0 -> throw Exception("올바르지 않은 게시판에 접근함")
                    else -> callback.onResult(
                        response.results,
                        Notices.getPage(response.previous)
                    )
                }
            } catch (exception: Exception) {
                Log.e("GetNoticeData", "Failed to fetch data")
                viewModel.endLoad()
                viewModel.showError()
            }
        }
    }

    override fun invalidate() {
        super.invalidate()
        viewModelScope.cancel()
    }
}