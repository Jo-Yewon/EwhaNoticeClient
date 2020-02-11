package com.ake.ewhanoticeclient.activity_setting

import android.content.Context
import android.content.Intent
import android.net.Uri
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
            BoardRepository.PREFERENCES_NAME, Context.MODE_PRIVATE
        )
        val repository = BoardRepository.getInstance(dao, sharedPreferences)

        val binding: ActivitySettingBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_setting)
        binding.lifecycleOwner = this

        val viewModel = ViewModelProviders.of(this, SettingViewModelFactory(repository))
            .get(SettingViewModel::class.java)
        binding.viewModel = viewModel

        // Observing navigation
        viewModel.navigate.observe(this, Observer {
            it?.let {
                viewModel.endNavigate()
                when (it) {
                    SettingViewModel.SUBSCRIBE ->
                        startActivity(Intent(this, SubscribeActivity::class.java))
                    SettingViewModel.BACK ->
                        finish()
                    SettingViewModel.GITHUB ->
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://github.com/Jo-Yewon/EwhaNoticeClient")
                            )
                        )
                    SettingViewModel.REPORT ->{
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:yeawonjo@gmail.com")
                        }
                        startActivity(intent)
                    }
                }
            }
        })
    }
}