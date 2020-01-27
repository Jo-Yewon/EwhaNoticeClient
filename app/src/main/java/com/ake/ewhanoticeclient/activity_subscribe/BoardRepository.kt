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

    fun setSubscribedBoardList(boards: MutableList<Board>) {
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

    suspend fun getUnsubscribedBoardsFromDatabase(): List<Board>{
        return withContext(Dispatchers.IO){
            val subscribedBoards = getSubscribedBoardList()
            val unsubscribedBoards = boardDatabase.getAllBoards()

            if (subscribedBoards != null) {
                unsubscribedBoards.filter {
                    for (subscribedBoard in subscribedBoards)
                        if (subscribedBoard.boardId == it.boardId)
                            false
                    true
                }
            }
            unsubscribedBoards
        }
    }

    suspend fun getBoardFromDatabase(boardId:Int): Board{
        return withContext(Dispatchers.IO){
            boardDatabase.getBoard(boardId)
        }
    }

}