package com.ake.ewhanoticeclient.network

import com.ake.ewhanoticeclient.database.DatabaseNotice
import com.google.gson.annotations.SerializedName

data class NetworkNotice(
    val num: Int,
    val title: String,
    val category: String?,
    val date: String,
    val url: String
)

data class NetworkNotices(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<NetworkNotice>
)

fun NetworkNotices.asDatabaseModel(boardId: Int): List<DatabaseNotice>{
    return results.map {
        DatabaseNotice(
            boardId,
            it.num,
            it.title,
            it.category,
            it.date,
            it.url)
    }
}

data class NetworkBoardURL(
    @SerializedName("base_url") val baseUrl: String,
    @SerializedName("next_url") val nextUrl: String
)
