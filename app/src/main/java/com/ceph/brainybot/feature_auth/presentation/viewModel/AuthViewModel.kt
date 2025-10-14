package com.ceph.brainybot.feature_auth.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ceph.brainybot.feature_auth.domain.model.UserData
import com.ceph.brainybot.feature_auth.domain.repository.AuthRepository
import com.ceph.brainybot.feature_auth.presentation.signIn.SignInState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private var _state = MutableStateFlow<SignInState>(SignInState.IsIdle)
    val state = _state.asStateFlow()

    init {
        getCurrentUser()
    }


    fun signInWithGoogle() {
        viewModelScope.launch {
            val result = authRepository.signInWithGoogle()
            if (result.userData != null) {
                _state.value = SignInState.Success(result.userData)
            } else {
                _state.value = SignInState.IsError
            }

        }
    }

    fun createUserWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            _state.value = SignInState.IsLoading
            val result = authRepository.createUserWithEmailAndPassword(email, password)
            if (result.userData != null) {
                _state.value = SignInState.Success(result.userData)
            } else {
                _state.value = SignInState.IsError
            }
        }
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            _state.value = SignInState.IsLoading
            val result = authRepository.signInWithEmailAndPassword(email, password)
            if (result.userData != null) {
                _state.value = SignInState.Success(result.userData)
            } else {
                _state.value = SignInState.IsError
            }
        }


    }

    fun getCurrentUser(): UserData? {

        var user: UserData? = null

        viewModelScope.launch {
            user = authRepository.getCurrentUser()
            if (user != null) {
                _state.value = SignInState.Success(user!!)
            } else {
                _state.value = SignInState.IsError
            }
        }

        return user

    }

    suspend fun signOut() {
        try {
            authRepository.signOut()
            _state.value = SignInState.IsIdle
        } catch (e: Exception) {
            throw e
        }

    }

}