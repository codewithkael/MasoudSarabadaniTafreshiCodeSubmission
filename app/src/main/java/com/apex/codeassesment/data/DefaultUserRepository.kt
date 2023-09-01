package com.apex.codeassesment.data

import com.apex.codeassesment.data.local.LocalDataSourceKt
import com.apex.codeassesment.data.model.User
import com.apex.codeassesment.data.remote.RemoteDataSource
import javax.inject.Inject

// TODO (2 points) : Add tests
// TODO (3 points) : Hide this class through an interface, inject the interface in the clients instead and remove warnings
class DefaultUserRepository @Inject constructor(
    private val localDataSource: LocalDataSourceKt,
    private val remoteDataSource: RemoteDataSource
) : UserRepository {

//    private val savedUser = AtomicReference(User())

    override fun getSavedUser() = localDataSource.loadUser()

    override suspend fun getUser(forceUpdate: Boolean): User {
        return if (forceUpdate) {
            when(val user = remoteDataSource.loadUserFromRemote()){
                is Response.Success -> {
                    localDataSource.saveUser(user.data)
                    user.data
                }
                is Response.Failure -> {
                    localDataSource.saveUser(user.data)
                    user.data
                }
            }

        } else {
            getSavedUser()
        }
    }

    override suspend fun getUsers(): List<User> {
        return when(val users = remoteDataSource.loadUsersFromRemote()){
            is Response.Success -> users.data
            is Response.Failure -> users.data
        }
    }
}
