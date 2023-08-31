package com.apex.codeassesment.data

import com.apex.codeassesment.data.local.LocalDataSource
import com.apex.codeassesment.data.model.User
import com.apex.codeassesment.data.remote.RemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

// TODO (2 points) : Add tests
// TODO (3 points) : Hide this class through an interface, inject the interface in the clients instead and remove warnings
class UserRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) {

  private val savedUser = AtomicReference(User())

  fun getSavedUser() = localDataSource.loadUser()

  fun getUser(forceUpdate: Boolean): User {
    if (forceUpdate) {
      CoroutineScope(Dispatchers.IO).launch {
        val user = remoteDataSource.loadUser()
        withContext(Dispatchers.Main){
          localDataSource.saveUser(user)
          savedUser.set(user)
        }
      }
    }
    return savedUser.get()
  }

  fun getUsers() = remoteDataSource.loadUsers()
}
