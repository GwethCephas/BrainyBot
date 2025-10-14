package com.ceph.brainybot.feature_auth.domain.model

data class SignInResult(
    val userData: UserData?,
    val errorMessage: String? = ""
)

data class UserData(
    val userId: String?,
    val username: String?,
    val profilePictureUrl: String?,
    val email: String?
)
