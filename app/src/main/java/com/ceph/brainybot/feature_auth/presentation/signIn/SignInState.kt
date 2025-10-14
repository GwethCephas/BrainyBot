package com.ceph.brainybot.feature_auth.presentation.signIn

import com.ceph.brainybot.feature_auth.domain.model.UserData

sealed class SignInState {
    data object IsIdle: SignInState()
    data object IsLoading : SignInState()
    data class Success(val userData: UserData) : SignInState()
    data object IsError : SignInState()
}