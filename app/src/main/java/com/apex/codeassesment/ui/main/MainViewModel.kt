package com.apex.codeassesment.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apex.codeassesment.data.UserRepository
import com.apex.codeassesment.ui.main.MainViewActions.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
class MainViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _viewEvents = MutableSharedFlow<MainViewEvents>()
    val viewEvents: SharedFlow<MainViewEvents> = _viewEvents
    private fun emmitEvent(event:MainViewEvents){
        viewModelScope.launch {
            _viewEvents.emit(event)
        }
    }

    private val _state:MutableLiveData<MainViewState> = MutableLiveData()
    val state:LiveData<MainViewState>
        get() = _state

    private fun getCurrentState():MainViewState{
        return state.value?:MainViewState()
    }

    fun handle(action:MainViewActions) {
        when(action){
            LoadSavedUser -> handleLoadSavedUser()
            is GetUser -> handleGetUser(action.isForce)
            GetUsers -> handleGetUsers()
        }
    }

    private fun handleGetUsers() {
        viewModelScope.launch {
            val users = userRepository.getUsers()
            _state.postValue(getCurrentState().copy(usersList = users))
        }
    }

    private fun handleLoadSavedUser(){
        val savedUser = userRepository.getSavedUser()
        _state.value = MainViewState(savedUser)
        emmitEvent(MainViewEvents.SavedUserLoaded)
    }

    private fun handleGetUser(isForce:Boolean){
        viewModelScope.launch {
           val user = userRepository.getUser(isForce)
            _state.postValue(getCurrentState().copy(currentUser = user))
        }
    }
}