package com.example.expensex

import android.content.Context

class SessionManager(context: Context) {
    private val prefs =
        context.getSharedPreferences("expensex_prefs", Context.MODE_PRIVATE)

    fun saveUid(uid: String) {
        prefs.edit().putString("uid", uid).apply()
    }

    fun getUid(): String? {
        return prefs.getString("uid", null)
    }

    fun clear() {
        prefs.edit().clear().apply()
    }
}