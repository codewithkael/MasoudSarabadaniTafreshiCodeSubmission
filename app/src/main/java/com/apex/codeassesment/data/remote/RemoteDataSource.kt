package com.apex.codeassesment.data.remote

import com.apex.codeassesment.data.model.User
import javax.inject.Inject

// TODO (2 points): Add tests
class RemoteDataSource @Inject constructor(
  private val usersDao: UsersDao
) {

  // TODO (5 points): Load data from endpoint https://randomuser.me/api
//  fun LoadUser() = User.createRandom()

  //returns dummy user in case of any failure
  suspend fun loadUser() : User {
    return try {
        usersDao.getUser().response.first()
    }catch (e:Exception){
      e.printStackTrace()
      User.createRandom()
    }
  }

  // TODO (3 points): Load data from endpoint https://randomuser.me/api?results=10
  // TODO (Optional Bonus: 3 points): Handle succes and failure from endpoints
  //returns list of dummy users in case of failure
  suspend fun loadUserFromRemote():List<User>{
    return try {
        usersDao.getUsers(10).response
    }catch (e:Exception){
      e.printStackTrace()
      loadUsers()
    }
  }
  fun loadUsers() = (0..10).map { User.createRandom() }
}
