package com.apex.codeassesment.data.local

import android.util.Log
import com.apex.codeassesment.data.model.Dob
import com.apex.codeassesment.data.model.Name
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
        val expectedUser = User(
            gender = "male", name = Name(title = "Mr", first = "Craig", last = "Gilbert"),
            dob = Dob(date = "1993-06-05T11:12:07.336Z", age = 30)
        )
        Log.d("TAG", "loadUser: ${jsonAdapter.toJson(expectedUser)}")
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