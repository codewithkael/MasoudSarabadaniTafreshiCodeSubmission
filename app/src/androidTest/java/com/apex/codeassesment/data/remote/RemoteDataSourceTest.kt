package com.apex.codeassesment.data.remote

import com.apex.codeassesment.data.Response
import com.apex.codeassesment.data.model.GetUserResponse
import com.apex.codeassesment.data.model.User
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RemoteDataSourceTest {

    private val usersDao: UsersDao = mockk()
    private lateinit var remoteDataSource: RemoteDataSource

    @Before
    fun setup() {
        remoteDataSource = RemoteDataSource(usersDao)
    }

    @Test
    fun testLoadUserSuccess() = runTest {
        val expectedUser = User.createRandom()
        coEvery { usersDao.getUser() } returns GetUserResponse(listOf(expectedUser))

        val response = remoteDataSource.loadUserFromRemote()
        assert(response is Response.Success)
        assertEquals((response as Response.Success).data, expectedUser)
    }

    @Test
    fun testLoadUsersFailure() = runTest {
        coEvery { usersDao.getUser() } throws Exception("No Network")
        val response = remoteDataSource.loadUserFromRemote()
        assert(response is Response.Failure)

    }

    @Test
    fun testLoadUsersFailureHastDefaultRandomUser() = runTest {
        coEvery { usersDao.getUser() } throws Exception("No Network")
        val response = remoteDataSource.loadUserFromRemote()

        //in case of failure network request, random user is being generated
        assert((response as Response.Failure).data.name?.first != null)
    }

    @Test
    fun testLoadUsersFromRemoteSuccess() = runTest {
        val expectedUsers = (0..9).map { User.createRandom() }
        coEvery { usersDao.getUsers(10) } returns GetUserResponse(expectedUsers)

        val response = remoteDataSource.loadUsersFromRemote()

        assert(response is Response.Success)
        assert((response as Response.Success).data == expectedUsers)
    }

    @Test
    fun testLoadUsersFromRemoteFailureHasDefaultRandomUsers() = runTest {
        coEvery { usersDao.getUsers(10) } throws Exception("No Internet")

        val response = remoteDataSource.loadUsersFromRemote()

        assert(response is Response.Failure)
        (response as Response.Failure).data.onEach {

            //in case of failure network request we have default users available
            assert(it.name?.first?.isNotEmpty() == true)
        }
    }
}