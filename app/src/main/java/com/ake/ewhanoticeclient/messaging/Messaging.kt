package com.ake.ewhanoticeclient.messaging

import android.content.SharedPreferences

class Messaging(private val sharedPreferences: SharedPreferences) {
    companion object{
        private const val PUSH = "push"
    }

    fun setPushStatus(push: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(PUSH, push)
        editor.apply()
    }

    fun getPushStatus(): Boolean {
        return sharedPreferences.getBoolean(PUSH, true)
    }
}