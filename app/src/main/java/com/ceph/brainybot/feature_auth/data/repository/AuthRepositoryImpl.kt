package com.ceph.brainybot.feature_auth.data.repository

import com.ceph.brainybot.feature_auth.data.auth.GoogleAuthClient
import com.ceph.brainybot.feature_auth.domain.repository.AuthRepository
import com.ceph.brainybot.feature_auth.domain.model.SignInResult
import com.ceph.brainybot.feature_auth.domain.model.UserData

class AuthRepositoryImpl(
    private val googleAuthClient: GoogleAuthClient
) : AuthRepository {

    override suspend fun signInWithGoogle(): SignInResult {
        return googleAuthClient.signInWithGoogle()
    }

    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): SignInResult =
        googleAuthClient.createUserWithEmailAndPassword(email, password)


    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): SignInResult {
        return googleAuthClient.signInWithEmailAndPassword(email, password)
    }

    override suspend fun getCurrentUser(): UserData? {
        return googleAuthClient.getCurrentUser()
    }

    override suspend fun signOut() {
        googleAuthClient.signOut()

    }
}