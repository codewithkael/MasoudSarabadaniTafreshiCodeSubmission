package com.apex.codeassesment.data.local

import com.apex.codeassesment.data.model.User
import com.apex.codeassesment.data.model.User.Companion.createRandom
import com.squareup.moshi.Moshi
import javax.inject.Inject


class LocalDataSourceKt @Inject constructor(
    moshi: Moshi,
    private val preferencesManager: PreferencesManager
) {
    private val jsonAdapter by lazy { moshi.adapter(User::class.java) }

    fun loadUser(): User {
        val serializedUser = preferencesManager.loadUser()
        return try {
            val user = serializedUser?.let { jsonAdapter.fromJson(it) }
            user ?: createRandom()
        } catch (e: Exception) {
            e.printStackTrace()
            createRandom()
        }
    }

    fun saveUser(user: User) {
        val serializedUser = jsonAdapter.toJson(user)
        preferencesManager.saveUser(serializedUser)
    }
}