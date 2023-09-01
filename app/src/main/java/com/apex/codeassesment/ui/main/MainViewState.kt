package com.apex.codeassesment.ui.main

import com.apex.codeassesment.data.model.User

data class MainViewState(
    val currentUser: User?=null,
    val usersList: List<User>?=null
)
