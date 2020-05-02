package com.ake.ewhanoticeclient.repositories

import android.content.SharedPreferences
import com.ake.ewhanoticeclient.database.BoardDatabaseDao
import com.ake.ewhanoticeclient.database.DatabaseBoard
import com.ake.ewhanoticeclient.domain.Board
import com.ake.ewhanoticeclient.domain.stringify
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BoardRepository(private val boardDatabase: BoardDatabaseDao,
                      private val sharedPreferences: SharedPreferences) {
    companion object {
        @Volatile
        private var instance: BoardRepository? = null

        fun getInstance(boardDatabase: BoardDatabaseDao, sharedPreferences: SharedPreferences) =
            instance
                ?: synchronized(this) {
                    instance
                        ?: BoardRepository(
                            boardDatabase,
                            sharedPreferences
                        ).also { instance = it }
                }

        const val PREFERENCES_NAME = "subscription"
        private const val KEY = "subscription"
    }

    fun getSubscribedBoardList(): List<Board>{
        return Board.getBoardsFromString(sharedPreferences.getString(KEY, ""))
    }

    fun subscribeBoards(boards: List<Board>){
        val subscribedBoards = getSubscribedBoardList().toMutableList()
        val outDatedCategory = subscribedBoards.map { it.boardCategory }
        val updatedCategory = boards.map { it.boardCategory }

        val firebaseMessaging = FirebaseMessaging.getInstance()
        for (category in outDatedCategory)
            if (category !in updatedCategory)
                firebaseMessaging.unsubscribeFromTopic(category)

        for (category in updatedCategory)
            if (category !in outDatedCategory)
                firebaseMessaging.subscribeToTopic(category)

        sharedPreferences.edit().apply {
            putString(KEY, boards.stringify())
        }.apply()
    }

    private var allBoards: List<DatabaseBoard>? = null

    suspend fun getBoardsFromDatabase(): List<DatabaseBoard> {
        return allBoards
            ?: withContext(Dispatchers.IO) {
                allBoards = boardDatabase.getAllBoards()
                allBoards
            }!!
    }

    suspend fun searchBoardsFromDatabase(keyword: String): List<DatabaseBoard> {
        return withContext(Dispatchers.IO) {
            boardDatabase.searchBoardByKeyword("%${keyword}%")
        }
    }
}