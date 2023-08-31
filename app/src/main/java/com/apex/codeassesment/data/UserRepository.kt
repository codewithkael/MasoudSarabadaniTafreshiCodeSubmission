package com.apex.codeassesment.data

import com.apex.codeassesment.data.model.User

interface UserRepository {
    fun getSavedUser():User
    suspend fun getUser(forceUpdate: Boolean): User
    suspend fun getUsers():List<User>
}