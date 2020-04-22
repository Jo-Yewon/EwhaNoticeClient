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
){
    companion object{
        private val categoryMap = hashMapOf(
            "Other" to "기타",
            "Common" to "전체",
            "Eltec" to "엘텍공과대학",
            "Music" to "음악대학",
            "Pharm" to "약학대학",
            "Apple" to "사회과학대학",
            "Nature" to "자연과학대학",
            "Convergence" to "신산업융합대학",
            "Liberal" to "인문과학대학",
            "Scranton" to "스크랜튼대학",
            "Nursing" to "간호대학",
            "Teachers" to "사범대학",
            "Med" to "의과대학",
            "Hokma" to "호크마")

        fun convertBoardCategory(boardCategory: String): String =
            categoryMap[boardCategory] ?: boardCategory
    }
}

fun List<DatabaseBoard>.asDomainModel(): List<Board> =
    map{
        Board(
            it.boardId,
            it.boardCategory,
            DatabaseBoard.convertBoardCategory(it.boardCategory),
            it.title,
            it.alias
        )
    }