package com.ake.ewhanoticeclient.database

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class BoardDatabaseTest {

    private lateinit var boardDao: BoardDatabaseDao
    private lateinit var db: BoardDatabase

    @Before
    fun createMockDB() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, BoardDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        boardDao = db.boardDatabaseDao

        val board1 = DatabaseBoard(1, "cate1", "sky", "white")
        val board2 = DatabaseBoard(2, "cate2", "blue", "long")
        val board3 = DatabaseBoard(3, "cate1", "curry her", "live")
        val board4 = DatabaseBoard(4, "cate2", "blue sky", "long")
        boardDao.insertBoard(board1)
        boardDao.insertBoard(board2)
        boardDao.insertBoard(board3)
        boardDao.insertBoard(board4)
    }

    @Test
    @Throws(Exception::class)
    fun getBoards() {
        val board = boardDao.getBoard(1)
        assertEquals(board.title, "sky")

        val boards = boardDao.getAllBoards()
        assertEquals(boards.size, 4)
    }

    @Test
    @Throws(Exception::class)
    fun searchBoards() {
        val boards = boardDao.searchBoardByKeyword("%blue%").map { it.boardId }
        assertEquals(boards.size, 2)
        assert(boards.containsAll(arrayListOf(2, 4)))

        val boards2 = boardDao.searchBoardByKeyword("%rr%")
        assertEquals(boards2[0].title, "curry her")
    }

    @Test
    @Throws(Exception::class)
    fun getTopics() {
        val topics = boardDao.getAllTopics()
        assertEquals(topics.size, 2)
        assert(topics.containsAll(arrayListOf("cate1", "cate2")))
    }

    @After
    @Throws(IOException::class)
    fun closeDB() {
        db.close()
    }
}