package com.example.expensex

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext context: Context
) {
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