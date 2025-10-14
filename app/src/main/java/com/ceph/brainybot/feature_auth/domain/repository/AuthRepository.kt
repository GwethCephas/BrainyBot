package com.ceph.brainybot.feature_auth.domain.repository

import androidx.credentials.Credential
import com.ceph.brainybot.feature_auth.domain.model.SignInResult
import com.ceph.brainybot.feature_auth.domain.model.UserData

interface AuthRepository {

    suspend fun signInWithGoogle(): SignInResult
    suspend fun createUserWithEmailAndPassword(email: String, password: String): SignInResult
    suspend fun signInWithEmailAndPassword(email: String, password: String): SignInResult
    suspend fun getCurrentUser(): UserData?
    suspend fun signOut()
}