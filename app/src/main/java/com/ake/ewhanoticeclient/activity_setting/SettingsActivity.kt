package com.ake.ewhanoticeclient.activity_setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ake.ewhanoticeclient.R
import com.ake.ewhanoticeclient.activity_subscribe.SubscribeActivity
import com.ake.ewhanoticeclient.database.BoardDatabase
import com.ake.ewhanoticeclient.database.BoardRepository
import com.ake.ewhanoticeclient.databinding.ActivitySettingBinding

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dao = BoardDatabase.getInstance(application).BoardDatabaseDao
        val sharedPreferences = getSharedPreferences(
            BoardRepository.PREFERENCES_NAME, Context.MODE_PRIVATE)
        val repository = BoardRepository.getInstance(dao, sharedPreferences)

        val binding: ActivitySettingBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_setting)
        binding.lifecycleOwner = this

        val viewModel = ViewModelProviders.of(this, SettingViewModelFactory(repository))
            .get(SettingViewModel::class.java)
        binding.viewModel = viewModel

        // Observing navigation
        viewModel.navigate.observe(this, Observer {
            if (it != SettingViewModel.DEFAULT) {
                viewModel.endNavigate()
                when (it) {
                    SettingViewModel.SUBSCRIBE ->
                        startActivity(Intent(this, SubscribeActivity::class.java))
                    SettingViewModel.BACK ->
                        finish()
                    SettingViewModel.INFO ->
                        TODO()
                }
            }
        })
    }
}