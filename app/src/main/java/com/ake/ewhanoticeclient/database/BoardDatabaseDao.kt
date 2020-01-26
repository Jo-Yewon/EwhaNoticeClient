package com.ake.ewhanoticeclient.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface BoardDatabaseDao{
    @Query("select * from board_table")
    fun getAllBoards(): List<Board>
}