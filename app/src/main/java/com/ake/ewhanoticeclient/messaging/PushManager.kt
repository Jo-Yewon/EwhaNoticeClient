package com.ake.ewhanoticeclient.messaging

import android.content.SharedPreferences

class PushManager(private val sharedPreferences: SharedPreferences) {
    companion object{
        private const val KEY = "push"
    }

    fun setPushStatus(push: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY, push)
        editor.apply()
    }

    fun getPushStatus(): Boolean {
        return sharedPreferences.getBoolean(KEY, true)
    }
}