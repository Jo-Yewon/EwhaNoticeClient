package com.ake.ewhanoticeclient.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "board_table")
data class Board(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "board_id")
    val boardId: Int,

    @ColumnInfo(name = "board_category")
    val boardCategory: String,

    val title: String,

    val alias: String
): Comparable<Board>, Serializable{
    override fun toString() = "${boardId},${boardCategory},${title},${alias}"

    companion object{
        fun getBoardFromString(boardString:String): Board{
            val st = StringTokenizer(boardString, ",")
            return Board(Integer.parseInt(st.nextToken()), st.nextToken(), st.nextToken(), st.nextToken())
        }
    }

    override fun compareTo(other: Board): Int {
        return boardId - other.boardId
    }
}