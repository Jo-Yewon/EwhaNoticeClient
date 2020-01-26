package com.ake.ewhanoticeclient.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "board_table")
data class Board(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "board_id")
    val boardId: Int,

    @ColumnInfo(name = "board_category")
    val boardCategory: String,

    val title: String,

    val description: String
)