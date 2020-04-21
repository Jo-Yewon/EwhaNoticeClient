package com.ake.ewhanoticeclient.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ServerApi {
    @GET("/notice/notices/{board_id}/")
    suspend fun getNotices(
        @Path("board_id") boardId: Int
    ): NetworkNotices

    @GET("/notice/boards/{board_id}/")
    suspend fun getURL(@Path("board_id") boardId: Int): NetworkBoardURL
}

object NoticeNetwork {
    private const val BASE_URL = "http://15.165.76.40/"

    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    val noticeNetwork: ServerApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ServerApi::class.java)
}