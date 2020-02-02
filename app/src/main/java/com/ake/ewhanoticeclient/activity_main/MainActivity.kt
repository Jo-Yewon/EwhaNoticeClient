package com.ake.ewhanoticeclient.activity_main

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.ake.ewhanoticeclient.R
import com.ake.ewhanoticeclient.database.BoardDatabase
import com.ake.ewhanoticeclient.database.BoardRepository
import com.ake.ewhanoticeclient.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        // Fragments
        val dao = BoardDatabase.getInstance(application).BoardDatabaseDao
        val sharedPreferences = getSharedPreferences(
            BoardRepository.PREFERENCES_NAME, Context.MODE_PRIVATE)
        val repository = BoardRepository.getInstance(dao, sharedPreferences)

        val sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager, repository)
        binding.viewPager.adapter = sectionsPagerAdapter

        binding.tabs.setupWithViewPager(binding.viewPager)
    }
}
