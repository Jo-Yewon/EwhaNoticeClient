package com.ake.ewhanoticeclient.repositories

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.ake.ewhanoticeclient.database.NoticeDatabase
import com.ake.ewhanoticeclient.database.asDomainModel
import com.ake.ewhanoticeclient.domain.Notice
import com.ake.ewhanoticeclient.network.NoticeNetwork
import com.ake.ewhanoticeclient.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NoticesRepository(
    private val boardId: Int,
    private val database: NoticeDatabase) {

    val notices: LiveData<PagedList<Notice>> =
        LivePagedListBuilder(database.noticeDatabaseDao.getNotices(boardId).map{
            it.asDomainModel()}, 10).build()

    suspend fun refreshNotices(){
        withContext(Dispatchers.IO){
            val updatedNotices = NoticeNetwork.noticeNetwork.getNotices(boardId).asDatabaseModel(boardId)
            val latest = updatedNotices.map { it.num }.max()
            database.noticeDatabaseDao.insertNotices(updatedNotices)
            if (latest != null) database.noticeDatabaseDao.deleteNotices(boardId, latest)
        }
    }
}