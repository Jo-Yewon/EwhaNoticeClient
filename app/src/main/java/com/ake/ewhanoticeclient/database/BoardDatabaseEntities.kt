package com.ake.ewhanoticeclient.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ake.ewhanoticeclient.domain.Board

@Entity(tableName = "board_table")
data class DatabaseBoard(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "board_id")
    val boardId: Int,

    @ColumnInfo(name = "board_category")
    val boardCategory: String,

    val title: String,
    val alias: String
)

fun List<DatabaseBoard>.asDomainModel(): List<Board> =
    map{
        Board(
            it.boardId,
            it.boardCategory,
            it.title,
            it.alias
        )
    }