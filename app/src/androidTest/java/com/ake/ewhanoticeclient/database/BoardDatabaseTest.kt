package com.ake.ewhanoticeclient.database

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import java.io.IOException

class BoardDatabaseTest {

    private lateinit var boardDao: BoardDatabaseDao
    private lateinit var db: BoardDatabase

    @Before
    fun createDB() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, BoardDatabase::class.java)
            .createFromAsset("boards.db")
            .allowMainThreadQueries()
            .build()
        boardDao = db.boardDatabaseDao
    }

    @After
    @Throws(IOException::class)
    fun closeDB() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun getBoards() {
        val boards = boardDao.getAllBoards()
        assertEquals(boards.size, 3)
    }
}