package com.ake.ewhanoticeclient.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.LivePagedListBuilder
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class NoticeDatabaseTest {

    private lateinit var noticeDao: NoticeDatabaseDao
    private lateinit var db: NoticeDatabase

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDB() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, NoticeDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        noticeDao = db.noticeDatabaseDao
    }

    @Test
    @Throws(Exception::class)
    fun writeAndReadNotices() {
        db.clearAllTables()

        // Write
        val notice1 = DatabaseNotice(
            1, 1, "test1",
            "cate1", "2020-03-22", "url1")
        val notice2 = DatabaseNotice(
            1, 2, "test2",
            "cate2", "2020-04-22", "url2")
        noticeDao.insertNotices(listOf(notice1, notice2))

        // Read
        val factory = noticeDao.getNotices(1)
        val pagedNotices = LivePagedListBuilder(factory, 10).build()
        pagedNotices.observeForever{
            assertEquals(it.size, 2)
            if (it[0] != null){
                assertEquals(it[0]!!.title, notice2.title)
                assertEquals(it[0]!!.num, notice2.num)
            }
            if (it[1] != null){
                assertEquals(it[1]!!.title, notice1.title)
                assertEquals(it[1]!!.num, notice1.num)
            }
        }
    }

    @Test
    @Throws(Exception::class)
    fun replaceNotice() {
        db.clearAllTables()

        // Write
        val notice1 = DatabaseNotice(
            1, 1, "before",
            "before", "2020-03-22", "url_before")
        noticeDao.insertNotices(listOf(notice1))

        // Replace - conflict on primary keys(boardId, num)
        val notice2 = DatabaseNotice(
            1, 1, "after",
            "after", "2020-03-22", "url_after")
        noticeDao.insertNotices(listOf(notice1, notice2))

        // Read
        val factory = noticeDao.getNotices(1)
        val pagedNotices = LivePagedListBuilder(factory, 10).build()
        pagedNotices.observeForever{
            if (it[0] != null)
                assertEquals(it[0]!!.title, notice2.title)
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDB() {
        db.close()
    }
}