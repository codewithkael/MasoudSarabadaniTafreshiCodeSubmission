package com.apex.codeassesment.data.local

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Before
import org.junit.Test

class PreferenceManagerTest {

    private lateinit var preferencesManager: PreferencesManager

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        preferencesManager = PreferencesManager(context)
    }

    @Test
    fun testSaveAndLoadUser() {
        val user = "Alice"
        preferencesManager.saveUser(user)
        val loadedUser = preferencesManager.loadUser()
        assert(user == loadedUser)
    }


    @Test
    fun testLoadUserWithoutSaving() {
        val loadedUser = preferencesManager.loadUser()
        assert(loadedUser != null)
    }
}