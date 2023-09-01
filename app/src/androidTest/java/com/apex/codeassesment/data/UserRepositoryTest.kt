package com.apex.codeassesment.data

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.apex.codeassesment.data.local.LocalDataSourceKt
import com.apex.codeassesment.data.local.PreferencesManager
import com.apex.codeassesment.data.model.GetUserResponse
import com.apex.codeassesment.data.model.User
import com.apex.codeassesment.data.remote.RemoteDataSource
import com.apex.codeassesment.data.remote.UsersDao
import com.squareup.moshi.Moshi
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryTest {

    private val moshi = Moshi.Builder().build()
    private val usersDao = mockk<UsersDao>()
    private lateinit var preferencesManager: PreferencesManager
    private lateinit var localDataSource: LocalDataSourceKt
    private lateinit var userRepository: DefaultUserRepository
    private lateinit var remoteDataSource :RemoteDataSource

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        preferencesManager = PreferencesManager(context)
        remoteDataSource = RemoteDataSource(usersDao)
        localDataSource = LocalDataSourceKt(moshi, preferencesManager)
        userRepository = DefaultUserRepository(localDataSource, remoteDataSource)
    }

    @Test
    fun testGetSavedUser() {
        val expectedUser = User.createRandom()
        localDataSource.saveUser(expectedUser)
        val actualUser = userRepository.getSavedUser()
        assert(expectedUser == actualUser)
    }

    @Test
    fun testGetUserForceUpdateSuccess() = runTest {
        val expectedUser = User.createRandom()
        val localUser = User.createRandom()
        localDataSource.saveUser(localUser)
        coEvery { usersDao.getUser() } returns GetUserResponse(listOf(expectedUser))

        val actualUser = userRepository.getUser(true)

        assert(actualUser!=localUser)
        assert(actualUser==expectedUser)
    }

    @Test
    fun testGetUserForceUpdateFailure() = runTest {
        coEvery { usersDao.getUser() } throws Exception("No internet")
        val localUser = User.createRandom()
        localDataSource.saveUser(localUser)

        val actualUser = userRepository.getUser(true)

        //event if request fails, we should have random user generated
        assert(actualUser.name?.first?.isNotEmpty()==true)
        assert(actualUser!=localUser)

    }

    @Test
    fun testGetUserNoForceUpdate() = runTest{
        val expectedUser = User.createRandom()
        coEvery { usersDao.getUser() } returns GetUserResponse(listOf(expectedUser))
        val localUser = User.createRandom()
        localDataSource.saveUser(localUser)

        val actualUser = userRepository.getUser(false)

        assert(actualUser!=expectedUser)
        assert(actualUser==localUser)
    }

    @Test
    fun testGetUsersFromRemoteSuccess() = runTest {
        val expectedUsers = (1..9).map { User.createRandom() }
        coEvery { usersDao.getUsers(10) } returns GetUserResponse(expectedUsers)

        val actualUsers = userRepository.getUsers()

        assert(expectedUsers==actualUsers)
    }

    @Test
    fun testGetUsersFromRemoteFailure() = runTest {
        coEvery { usersDao.getUsers(10) } throws Exception("No internet")
        val actualUsers = userRepository.getUsers()

        assert(actualUsers.isNotEmpty())

        //even if request is failed, we still have list of random users
        actualUsers.forEach {
            assert(it.name?.first?.isNotEmpty()==true)
        }
    }
}