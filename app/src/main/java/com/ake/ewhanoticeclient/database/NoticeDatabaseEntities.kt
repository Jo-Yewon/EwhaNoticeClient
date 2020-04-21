package com.ake.ewhanoticeclient.database

import androidx.room.Entity
import com.ake.ewhanoticeclient.domain.Notice

@Entity(tableName = "notice_table", primaryKeys = ["boardId", "num"])
data class DatabaseNotice(
    val boardId: Int,
    val num: Int,
    val title: String,
    val category: String?,
    val date: String,
    val url: String
)

fun DatabaseNotice.asDomainModel(): Notice =
        Notice(num, title, category, date, url)