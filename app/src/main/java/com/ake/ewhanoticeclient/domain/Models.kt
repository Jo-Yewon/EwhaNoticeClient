package com.ake.ewhanoticeclient.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Board(
    val boardId: Int,
    val boardCategory: String,
    val boardCategoryKorean: String,
    val title: String,
    val alias: String
):Comparable<Board>, Parcelable{
    override fun toString() = "${boardId},${boardCategory},${title},${alias}"
    override fun compareTo(other: Board) = boardId - other.boardId
}

fun List<Board>.stringify() = map {"$it"}.reduce{b1, b2 -> "${b1}/${b2}"}

data class SimpleNotice(
    val boardId: Int,
    val title: String) {

    companion object {
        fun getSimpleNoticeFromString(string: String): SimpleNotice? {
            for ((index, value) in string.withIndex())
                if (value == '&') {
                    return SimpleNotice(
                        string.substring(0, index).toInt(),
                        string.substring(index + 1).replace("\n", "")
                    )
                }
            return null
        }
    }
}

data class Notice(
    val num: Int,
    val title: String,
    val category: String?,
    val date: String,
    val url: String
)