package com.ake.ewhanoticeclient.activity_main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.ake.ewhanoticeclient.R
import com.ake.ewhanoticeclient.databinding.ActivityMainBinding
import com.ake.ewhanoticeclient.activity_main.SectionsPagerAdapter

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        val sectionsPagerAdapter = SectionsPagerAdapter(
            this,
            supportFragmentManager
        )
        binding.viewPager.adapter = sectionsPagerAdapter

        binding.tabs.setupWithViewPager(binding.viewPager)
    }
}
