package com.ake.ewhanoticeclient.network

import java.util.regex.Pattern

data class Notice(
    val num: Int,
    val title: String,
    val category: String?,
    val date: String,
    val url: String
)

data class Notices(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Notice>
){
    companion object{
        private val pattern = Pattern.compile("Page=")

        fun getPage(url: String?): Int?{
            if (url == null) return null
            val matcher = pattern.matcher(url)
            return when(matcher.find()){
                true -> Integer.parseInt(url.substring(matcher.end()))
                false -> 1
            }
        }
    }
}