package com.apex.codeassesment.data.local

import android.content.Context
import javax.inject.Inject

// TODO (2 point): Add tests
class PreferencesManager @Inject constructor(
    private val context: Context
) {

    fun saveUser(user: String) {
        val sharedPreferences =
            context.getSharedPreferences("random-user-preferences", Context.MODE_PRIVATE)
        sharedPreferences?.edit()?.putString("saved-user", user)?.apply()
    }

    fun loadUser(): String? {
        val sharedPreferences =
            context.getSharedPreferences("random-user-preferences", Context.MODE_PRIVATE)
        return sharedPreferences?.getString("saved-user", null)
    }
}
