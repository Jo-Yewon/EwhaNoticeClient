package com.ake.ewhanoticeclient.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface BoardDatabaseDao{
    @Query("select * from board_table")
    fun getAllBoards(): MutableList<Board>

    @Query("select * from board_table where board_id = :boardId")
    fun getBoard(boardId:Int): Board
}