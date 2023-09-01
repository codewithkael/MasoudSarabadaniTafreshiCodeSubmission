package com.apex.codeassesment.data.local

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.apex.codeassesment.data.model.Dob
import com.apex.codeassesment.data.model.Name
import com.apex.codeassesment.data.model.User
import com.squareup.moshi.Moshi
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LocalDataSourceTest {

    private lateinit var preferencesManager:PreferencesManager

    private var moshi: Moshi = Moshi.Builder().build()

    private lateinit var localDataSource: LocalDataSource

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        preferencesManager = PreferencesManager(context)
        localDataSource = LocalDataSource(preferencesManager,moshi)
    }

    @Test
    fun testSavingUser() {
        localDataSource.saveUser(User(name = Name(first = "Masoud")))
        assertNotNull(localDataSource.loadUser())
    }
    @Test
    fun testLoadingUserUsingSerializedString() {
        val serializedUser =
            "{\"gender\":\"male\",\"name\":{\"title\":\"Mr\",\"first\":\"Craig\",\"last\":\"Gilbert\"}" +
                    ",\"dob\":{\"date\":\"1993-06-05T11:12:07.336Z\",\"age\":30}}"
        preferencesManager.saveUser(serializedUser)

        val expectedUser = User(
            gender = "male", name = Name(title = "Mr", first = "Craig", last = "Gilbert"),
            dob = Dob(date = "1993-06-05T11:12:07.336Z", age = 30)
        )
        val actualUser = localDataSource.loadUser()
        assert(actualUser==expectedUser)
    }

    @Test
    fun testLoadingUserUsingActualModel() {
        val userToSave = User(
            gender = "male", name = Name(title = "Mr", first = "Craig", last = "Gilbert"),
            dob = Dob(date = "1993-06-05T11:12:07.336Z", age = 30)
        )
        localDataSource.saveUser(userToSave)

        val actualUser = localDataSource.loadUser()
        assert(actualUser==userToSave)
    }

    @Test
    fun testLoadUserWithInvalidData(){
        val serializedUser = "invalid json"
        preferencesManager.saveUser(serializedUser)

        val actualUser = localDataSource.loadUser()

        //we know that random generated user has a first name as default
        assertTrue(actualUser.name?.first?.isNotEmpty()==true)
    }
}

