package com.ake.ewhanoticeclient.activity_start

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.ake.ewhanoticeclient.R
import com.ake.ewhanoticeclient.activity_main.MainActivity
import com.ake.ewhanoticeclient.activity_subscribe.SubscribeActivity
import com.ake.ewhanoticeclient.database.BoardDatabase
import com.ake.ewhanoticeclient.repositories.BoardRepository
import com.ake.ewhanoticeclient.databinding.ActivityStartBinding
import com.ake.ewhanoticeclient.domain.Board
import com.ake.ewhanoticeclient.messaging.Messaging

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView(this, R.layout.activity_start) as ActivityStartBinding

        val dao = BoardDatabase.getInstance(application).boardDatabaseDao
        val sharedPreferences =
            getSharedPreferences(BoardRepository.PREFERENCES_NAME, Context.MODE_PRIVATE)
        val repository = BoardRepository.getInstance(dao, sharedPreferences)

        val subscribedBoards = repository.getSubscribedBoardList()

        if (subscribedBoards.isEmpty()) {
            Messaging(sharedPreferences).setPushStatus(true)  // Default true
            startActivity(Intent(this, SubscribeActivity::class.java))
        } else {
            // (update) 화학나노과학과의 게시판이 화생분으로 설정된 DB 수정 확인 과정
            var needUpdate = false
            val updatedSubscribedBoards = mutableListOf<Board>()
            for (board in subscribedBoards) {
                updatedSubscribedBoards.add(
                    if (board.boardId == 2153220 && board.alias == "화생분공지") {
                        needUpdate = true
                        Board(2153220, "Nature",
                            "화학나노과학과공지사항", "화학나노과학공지")
                    } else if (board.boardId == 2153221 && board.alias == "화생분채용") {
                        needUpdate = true
                        Board(2153221, "Nature",
                            "화학나노과학과채용정보", "화학나노과학채용")
                    } else board
                )
            }
            if (needUpdate) repository.setSubscribedBoardList(updatedSubscribedBoards)
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
