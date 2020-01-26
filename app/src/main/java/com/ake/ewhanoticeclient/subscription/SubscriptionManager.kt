package com.ake.ewhanoticeclient.subscription

import android.content.Context
import android.content.SharedPreferences
import com.ake.ewhanoticeclient.database.Board
import java.util.*

object SubscriptionManager {
    private const val PREFERENCES_NAME = "subscription"
    private const val KEY = "subscription"

    private var subscriptionBoardList: MutableList<Board>? = null

    fun getPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun setSubscribedBoardList(context: Context, boardList: MutableList<Board>) {
        subscriptionBoardList = boardList
        var boardsString = ""
        for (board in boardList)
            boardsString += board.toString()

        val editor = getPreferences(context).edit()
        editor.putString(KEY, boardsString)
        editor.commit()
    }

    fun getSubscribedBoardList(context: Context): MutableList<Board> {
        if (subscriptionBoardList == null) {
            val boardsString = getPreferences(context).getString(KEY, null)

            subscriptionBoardList = when (boardsString) {
                null -> mutableListOf()
                else -> {
                    val list = mutableListOf<Board>()
                    val st = StringTokenizer(boardsString, ",")
                    while (st.hasMoreTokens())
                        list.add(Board.getBoardFromString(st.nextToken()))
                    list
                }
            }
        }
        return subscriptionBoardList!!
    }
}