package com.ake.ewhanoticeclient.activity_main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ake.ewhanoticeclient.R
import com.ake.ewhanoticeclient.activity_setting.SettingsActivity
import com.ake.ewhanoticeclient.database.BoardDatabase
import com.ake.ewhanoticeclient.repositories.BoardRepository
import com.ake.ewhanoticeclient.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    interface CommonBoardFragment{
        fun onBackPressed(): Boolean }
    private var common: CommonBoardFragment? = null

    private val dao by lazy { BoardDatabase.getInstance(application).boardDatabaseDao }
    private val sharedPreferences by lazy {
        getSharedPreferences(BoardRepository.PREFERENCES_NAME, Context.MODE_PRIVATE)}
    private val boardRepository by lazy { BoardRepository.getInstance(dao, sharedPreferences) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.viewModel = viewModel

        // Observing
        viewModel.navigateToSetting.observe(this, Observer {
            if (it){
                viewModel.endNavigateToSetting()
                startActivity(Intent(this, SettingsActivity::class.java))
            }
        })

        // Tab + ViewPager
        val sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager, lifecycle, boardRepository)
        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = sectionsPagerAdapter.getItemAlias(position)
        }.attach()
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
