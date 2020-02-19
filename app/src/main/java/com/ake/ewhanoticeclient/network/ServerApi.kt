package com.ake.ewhanoticeclient.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ServerApi {
    @GET("/notice/notices/{board_id}/")
    suspend fun getNoticesWithPage(
        @Path("board_id") boardId: Int,
        @Query("page") page: Int
    ): Notices

    @GET("/notice/boards/{board_id}/")
    suspend fun getURL(@Path("board_id") boardId: Int): BoardURL

    companion object {
        private const val BASE_URL = "http://15.165.76.40/"

        fun create(): ServerApi {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ServerApi::class.java)
        }
    }
}