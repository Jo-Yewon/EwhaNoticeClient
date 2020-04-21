package com.ake.ewhanoticeclient.activity_main

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.ake.ewhanoticeclient.database.NoticeDatabase
import com.ake.ewhanoticeclient.domain.Board
import com.ake.ewhanoticeclient.domain.Notice
import com.ake.ewhanoticeclient.network.NoticeNetwork
import com.ake.ewhanoticeclient.repositories.NoticesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class NoticePageViewModel(
    private val board: Board, application: Application) : AndroidViewModel(application) {

    private val noticeRepository = NoticesRepository(
        board.boardId,
        NoticeDatabase.getInstance(application))

    var notices: LiveData<PagedList<Notice>> = noticeRepository.notices

    private var _url = MutableLiveData<String?>()
    val url: LiveData<String?>
        get() = _url

    private var _expandBoard = MutableLiveData<String>()
    val expandBoard: LiveData<String>
        get() = _expandBoard

    private var _toast = MutableLiveData<String>()
    val toast: LiveData<String>
        get() = _toast

    private var _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean>
        get() = _isRefreshing

    init {
        _url.value = null
        _expandBoard.value = null
        _toast.value = null
        _isRefreshing.value = false
        refreshNotice()
    }

    fun showNotice(notice: Notice) {
        _url.value = notice.url
    }

    fun endShowNotice() {
        _url.value = null
    }

    fun refreshNotice() {
        viewModelScope.launch {
            try {
                _isRefreshing.value = true
                noticeRepository.refreshNotices()
                _isRefreshing.value = false
            } catch (networkError: IOException) {
                _isRefreshing.value = false
                _toast.value = "네트워크 오류"
            }
        }
    }

    fun expandBoard() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val boardUrl = NoticeNetwork.noticeNetwork.getURL(board.boardId)
                    withContext(Dispatchers.Main) {
                        _expandBoard.value = "${boardUrl.baseUrl}${boardUrl.nextUrl}"
                    }
                } catch (e: Exception) {
                    _toast.value = "웹 사이트를 열 수 없습니다."
                }
            }
        }
    }

    fun endExpand() {
        _expandBoard.value = null
    }

    fun endToast() {
        _toast.value = null
    }
}