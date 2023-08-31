package com.apex.codeassesment.di

import android.content.Context
import android.content.SharedPreferences
import com.apex.codeassesment.data.DefaultUserRepository
import com.apex.codeassesment.data.UserRepository
import com.apex.codeassesment.data.local.PreferencesManager
import com.apex.codeassesment.data.remote.UsersDao
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
object MainModule {

    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("random-user-preferences", Context.MODE_PRIVATE)
    }

    @Provides
    fun providePreferencesManager(): PreferencesManager = PreferencesManager()

    @Provides
    fun providesMoshi(): Moshi = Moshi.Builder().build()

    @Provides
    fun providesRetrofit(): Retrofit = Retrofit.Builder().baseUrl("https://randomuser.me")
        .addConverterFactory(MoshiConverterFactory.create()).build()

    @Provides
    fun provideUsersDao(retrofit: Retrofit): UsersDao {
        return retrofit.create(UsersDao::class.java)
    }

    @Provides
    fun provideUserRepository(userRepository: DefaultUserRepository): UserRepository =
        userRepository

}