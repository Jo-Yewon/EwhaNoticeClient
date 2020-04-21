package com.ake.ewhanoticeclient.repositories

import android.content.SharedPreferences
import com.ake.ewhanoticeclient.database.BoardDatabaseDao
import com.ake.ewhanoticeclient.database.DatabaseBoard
import com.ake.ewhanoticeclient.domain.Board
import com.ake.ewhanoticeclient.domain.stringify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class BoardRepository(
    private val boardDatabase: BoardDatabaseDao,
    private val sharedPreferences: SharedPreferences
) {
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

    // for legacy
    private fun getBoardFromString(boardString: String): Board{
        val st = StringTokenizer(boardString, ",")
        return Board(Integer.parseInt(st.nextToken()), st.nextToken(), st.nextToken(), st.nextToken())
    }

    fun setSubscribedBoardList(boards: List<Board>) {
        sharedPreferences.edit().apply{
            putString(KEY, boards.stringify())
        }.apply()
    }

    fun getSubscribedBoardList(): List<Board> =
        when (val boardsString = sharedPreferences.getString(KEY, null)) {
            null -> listOf()
            else -> {
                val list = mutableListOf<Board>()
                val st = StringTokenizer(boardsString, "/")
                while (st.hasMoreTokens())
                    with(st.nextToken()) {
                        if (this.isNotEmpty()) list.add(getBoardFromString(this))
                    }
                list
            }
        }

    suspend fun getBoardsFromDatabase(): List<DatabaseBoard> {
        return withContext(Dispatchers.IO) {
            boardDatabase.getAllBoards()
        }
    }

    suspend fun searchBoardsFromDatabase(keyword: String): List<DatabaseBoard> {
        return withContext(Dispatchers.IO) {
            boardDatabase.searchBoardByKeyword("%${keyword}%")
        }
    }

    suspend fun getAllTopics(): List<String> {
        return withContext(Dispatchers.IO) {
            boardDatabase.getAllTopics()
        }
    }
}