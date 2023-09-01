package com.apex.codeassesment.ui.main

sealed interface MainViewActions {
    object LoadSavedUser:MainViewActions
    data class GetUser(val isForce: Boolean): MainViewActions
    object GetUsers:MainViewActions

}