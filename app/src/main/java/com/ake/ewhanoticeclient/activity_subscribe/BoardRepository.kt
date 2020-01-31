package com.ake.ewhanoticeclient.activity_subscribe

import android.content.SharedPreferences
import com.ake.ewhanoticeclient.database.Board
import com.ake.ewhanoticeclient.database.BoardDatabaseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class BoardRepository private constructor(
    private val boardDatabase: BoardDatabaseDao,
    private val sharedPreferences: SharedPreferences) {

    companion object {
        @Volatile
        private var instance: BoardRepository? = null

        fun getInstance(boardDatabase: BoardDatabaseDao, sharedPreferences: SharedPreferences) =
            instance ?: synchronized(this) {
                instance ?: BoardRepository(boardDatabase, sharedPreferences).also { instance = it }
            }

        const val PREFERENCES_NAME = "subscription"
        private const val KEY = "subscription"
    }

    fun setSubscribedBoardList(boards: List<Board>) {
        var boardsString = ""
        for (board in boards)
            boardsString += board.toString()

        val editor = sharedPreferences.edit()
        editor.putString(KEY, boardsString)
        editor.commit()
    }

    fun getSubscribedBoardList(): List<Board> {
        val boardsString = sharedPreferences.getString(KEY, null)
        return when (boardsString) {
            null -> listOf()
            else -> {
                val list = mutableListOf<Board>()
                val st = StringTokenizer(boardsString, ",")
                while (st.hasMoreTokens())
                    list.add(Board.getBoardFromString(st.nextToken()))
                list
            }
        }
    }

    suspend fun getBoardsFromDatabase(): List<Board>{
        return withContext(Dispatchers.IO){
            boardDatabase.getAllBoards()
        }
    }

    suspend fun searchBoardsFromDatabase(keyword: String): List<Board>{
        return withContext(Dispatchers.IO){
            boardDatabase.searchBoardByKeyword("%${keyword}%")
        }
    }
}