package com.ake.ewhanoticeclient.activity_main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ake.ewhanoticeclient.R
import com.ake.ewhanoticeclient.activity_setting.SettingsActivity
import com.ake.ewhanoticeclient.database.BoardDatabase
import com.ake.ewhanoticeclient.database.BoardRepository
import com.ake.ewhanoticeclient.databinding.ActivityMainBinding
import com.google.android.gms.ads.AdRequest


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.navigateToSetting.observe(this, Observer {
            if (it){
                viewModel.endNavigateToSetting()
                startActivity(Intent(this, SettingsActivity::class.java))
            }
        })

        // Fragments
        val dao = BoardDatabase.getInstance(application).BoardDatabaseDao
        val sharedPreferences = getSharedPreferences(
            BoardRepository.PREFERENCES_NAME, Context.MODE_PRIVATE)
        val repository = BoardRepository.getInstance(dao, sharedPreferences)

        val sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager, repository)
        binding.viewPager.adapter = sectionsPagerAdapter

        binding.tabs.setupWithViewPager(binding.viewPager)

        initAdmob()
    }

    private fun initAdmob(){
        binding.adView.loadAd(AdRequest.Builder().build())
    }
}
