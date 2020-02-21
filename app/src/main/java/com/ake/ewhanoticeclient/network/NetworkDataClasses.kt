package com.ake.ewhanoticeclient.network

import android.util.Log
import com.google.gson.annotations.SerializedName
import java.util.regex.Pattern

data class Notice(
    val num: Int,
    val title: String,
    val category: String?,
    val date: String,
    val url: String
){
    init{
        Log.d("notice", "init")
    }
}

data class Notices(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Notice>
){
    companion object{
        private val pattern = Pattern.compile("page=")

        fun getPage(url: String?): Int?{
            if (url == null) return null
            val matcher = pattern.matcher(url)
            return when(matcher.find()){
                true -> Integer.parseInt(url.substring(matcher.end()))
                false -> null
            }
        }
    }
}

data class BoardURL(
    @SerializedName("base_url") val baseUrl: String,
    @SerializedName("next_url") val nextUrl: String
)