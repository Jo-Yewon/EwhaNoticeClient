package com.ake.ewhanoticeclient.activity_start

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ake.ewhanoticeclient.activity_main.MainActivity
import com.ake.ewhanoticeclient.activity_subscribe.SubscribeActivity
import com.ake.ewhanoticeclient.database.BoardDatabase
import com.ake.ewhanoticeclient.messaging.PushManager
import com.ake.ewhanoticeclient.repositories.BoardRepository

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //DataBindingUtil.setContentView(this, R.layout.activity_start) as ActivityStartBinding

        val dao = BoardDatabase.getInstance(application).boardDatabaseDao
        val sharedPreferences =
            getSharedPreferences(BoardRepository.PREFERENCES_NAME, Context.MODE_PRIVATE)
        val boardRepository = BoardRepository.getInstance(dao, sharedPreferences)

        when (boardRepository.getSubscribedBoardList().isEmpty()) {
            true -> {
                PushManager(sharedPreferences).setPushStatus(true)  // Default true
                startActivity(Intent(this, SubscribeActivity::class.java))
            }
            false -> startActivity(Intent(this, MainActivity::class.java))
        }

    }
}
