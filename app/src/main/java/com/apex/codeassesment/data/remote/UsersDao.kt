package com.apex.codeassesment.data.remote

import com.apex.codeassesment.data.model.GetUserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface UsersDao {
    @GET("api")
    suspend fun getUser():GetUserResponse

    @GET("api")
    suspend fun getUsers(
        @Query("results") number:String
    ):GetUserResponse
}