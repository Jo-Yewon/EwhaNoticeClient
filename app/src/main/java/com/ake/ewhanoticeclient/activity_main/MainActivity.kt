package com.ake.ewhanoticeclient.activity_main

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Switch
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import com.ake.ewhanoticeclient.R
import com.ake.ewhanoticeclient.activity_subscribe.SubscribeActivity
import com.ake.ewhanoticeclient.database.BoardDatabase
import com.ake.ewhanoticeclient.repositories.BoardRepository
import com.ake.ewhanoticeclient.databinding.ActivityMainBinding
import com.ake.ewhanoticeclient.messaging.PushManager
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.switch_item.view.*


class MainActivity : AppCompatActivity() {
    private lateinit var mFirebaseAnalytics: FirebaseAnalytics

    lateinit var binding: ActivityMainBinding

    interface CommonBoardFragment {
        fun onBackPressed(): Boolean
    }

    private var common: CommonBoardFragment? = null

    private val dao by lazy { BoardDatabase.getInstance(application).boardDatabaseDao }
    private val sharedPreferences by lazy {
        getSharedPreferences(BoardRepository.PREFERENCES_NAME, Context.MODE_PRIVATE)
    }
    private val boardRepository by lazy { BoardRepository.getInstance(dao, sharedPreferences) }
    private val pushManager by lazy { PushManager(sharedPreferences) }

    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var pushSwitch: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        initToolbar()
        initTabViewPager()
        initDrawer()
    }

    override fun onResume() {
        super.onResume()
        pushSwitch.isChecked = pushManager.getPushStatus()
    }

    fun setCommon(common: CommonBoardFragment?) {
        this.common = common
    }

    override fun onBackPressed() {
        if (common != null && (common as CommonBoardFragment).onBackPressed())
            return  // If webView is on
        else if (binding.drawer.isDrawerOpen(GravityCompat.START))
            binding.drawer.closeDrawer(GravityCompat.START)
        else super.onBackPressed()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        finish()
        startActivity(getIntent())
    }

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)
        drawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToggle.onConfigurationChanged(newConfig)
    }

    private fun initTabViewPager() {
        val sectionsPagerAdapter =
            SectionsPagerAdapter(supportFragmentManager, lifecycle, boardRepository)
        binding.viewPager.adapter = sectionsPagerAdapter
        binding.viewPager.offscreenPageLimit = 5
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = sectionsPagerAdapter.getItemAlias(position)
        }.attach()
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        with(supportActionBar!!) {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }
    }

    private fun initDrawer() {
        drawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawer,
            binding.toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )
        binding.drawer.addDrawerListener(drawerToggle)

        // Set navigation
        binding.navViewOut.navViewIn.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.subscribe -> startActivity(Intent(this, SubscribeActivity::class.java))
                R.id.license -> {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://github.com/Jo-Yewon/EwhaNoticeClient")
                        )
                    )
                }
                R.id.report -> {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:yeawonjo@gmail.com")
                    }
                    startActivity(intent)
                }
                else -> 0
            }
            binding.drawer.closeDrawer(GravityCompat.START)
            true
        }

        // Set push switch
        pushSwitch =
            binding.navViewOut.navViewIn.menu.findItem(R.id.pushSwitch).actionView.switchWidget as Switch
        pushSwitch.isChecked = pushManager.getPushStatus()
        pushSwitch.setOnCheckedChangeListener { _, isChecked ->
            pushManager.setPushStatus(isChecked)
        }
    }
}
