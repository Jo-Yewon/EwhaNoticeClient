package com.ake.ewhanoticeclient.database

import android.content.SharedPreferences
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
                instance
                    ?: BoardRepository(
                        boardDatabase,
                        sharedPreferences
                    ).also { instance = it }
            }

        const val PREFERENCES_NAME = "subscription"
        private const val KEY = "subscription"
    }

    fun setSubscribedBoardList(boards: List<Board>) {
        var boardsString = ""
        for (board in boards)
            boardsString += "${board}/"

        val editor = sharedPreferences.edit()
        editor.putString(KEY, boardsString.removeSuffix("/"))
        editor.commit()
    }

    fun getSubscribedBoardList(): List<Board> {
        val boardsString = sharedPreferences.getString(KEY, null)
        return when (boardsString) {
            null -> listOf()
            else -> {
                val list = mutableListOf<Board>()
                val st = StringTokenizer(boardsString, "/")
                while (st.hasMoreTokens())
                    with(st.nextToken()){
                        if (this.isNotEmpty()) list.add(Board.getBoardFromString(this))
                    }
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