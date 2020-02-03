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
import com.ake.ewhanoticeclient.database.BoardRepository
import com.ake.ewhanoticeclient.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView(this, R.layout.activity_start) as ActivityStartBinding

        val dao = BoardDatabase.getInstance(application).BoardDatabaseDao
        val sharedPreferences = getSharedPreferences(
            BoardRepository.PREFERENCES_NAME, Context.MODE_PRIVATE)
        val repository = BoardRepository.getInstance(dao, sharedPreferences)

        startActivity(Intent(this, when(repository.getSubscribedBoardList().size){
            0 -> SubscribeActivity::class.java
            else -> MainActivity::class.java }))
    }
}
