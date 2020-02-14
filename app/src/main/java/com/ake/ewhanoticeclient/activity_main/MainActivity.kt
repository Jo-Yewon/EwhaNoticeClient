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



class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    interface CommonBoardFragment{
        fun onBackPressed(): Boolean }
    private var common: CommonBoardFragment? = null

    private val dao by lazy { BoardDatabase.getInstance(application).BoardDatabaseDao }
    private val sharedPreferences by lazy {
        getSharedPreferences(BoardRepository.PREFERENCES_NAME, Context.MODE_PRIVATE)}
    private val repository by lazy { BoardRepository.getInstance(dao, sharedPreferences) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        binding.viewModel = viewModel

        // Observing
        viewModel.navigateToSetting.observe(this, Observer {
            if (it){
                viewModel.endNavigateToSetting()
                startActivity(Intent(this, SettingsActivity::class.java))
            }
        })

        // Fragments
        val sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager, repository)
        binding.viewPager.adapter = sectionsPagerAdapter
        binding.tabs.setupWithViewPager(binding.viewPager)
    }

    fun setCommon(common: CommonBoardFragment?){
        // Connect common board fragment
        this.common = common
    }

    override fun onBackPressed() {
        // For webView in a common board fragment
        if (common != null && (common as CommonBoardFragment).onBackPressed()) return
        else super.onBackPressed()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        finish()
        startActivity(getIntent())
    }
}
