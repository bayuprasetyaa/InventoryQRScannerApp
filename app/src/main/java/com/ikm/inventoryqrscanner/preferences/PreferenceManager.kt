package com.ikm.inventoryqrscanner.preferences

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager (context: Context) {

    private val PREFS_NAME = "admin.pref"
    private var sharedPref: SharedPreferences
    val editor: SharedPreferences.Editor

    init {
        sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        editor = sharedPref.edit()
    }

    fun put(key: String, value: Boolean) {
        editor.putBoolean(key, value)
            .apply()
    }

    fun getBoolean(key: String): Boolean {
        return sharedPref.getBoolean(key, false)
    }

    fun clear() {
        editor.putBoolean("admin", false)
            .apply()
    }


}