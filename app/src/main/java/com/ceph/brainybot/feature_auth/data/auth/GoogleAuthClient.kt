package com.ceph.brainybot.feature_auth.data.auth

import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.ceph.brainybot.feature_auth.domain.model.SignInResult
import com.ceph.brainybot.feature_auth.domain.model.UserData
import com.ceph.brainybot.feature_auth.utils.Constants
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class GoogleAuthClient(
    private val context: Context
) {
    val firebaseAuth = FirebaseAuth.getInstance()
    val credentialManager = CredentialManager.Companion.create(context)


    suspend fun signInWithGoogle(): SignInResult {

        return try {

            val googleIdOption = GetGoogleIdOption.Builder()
                .setServerClientId(Constants.WEB_CLIENT)
                .setAutoSelectEnabled(false)
                .setFilterByAuthorizedAccounts(false)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val credentialResponse = credentialManager.getCredential(context, request)
            val credential = credentialResponse.credential

            authenticateWithFirebase(credential)

        } catch (e: Exception) {
            Log.e("GoogleAuthClient", "Error signing in", e)
            SignInResult(
                userData = null,
                errorMessage = e.message
            )
        }


    }


    private suspend fun authenticateWithFirebase(credential: Credential): SignInResult {

        return try {
            val googleIdTokenCredential =
                GoogleIdTokenCredential.Companion.createFrom(credential.data)
            val googleIdToken = googleIdTokenCredential.idToken

            if (googleIdToken.isEmpty()) {
                return SignInResult(null, "Google ID token is empty")
            }

            val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
            val user = firebaseAuth.signInWithCredential(firebaseCredential).await().user

            SignInResult(
                userData = user?.run {
                    UserData(
                        userId = uid,
                        username = displayName,
                        email = email,
                        profilePictureUrl = photoUrl.toString()
                    )
                },
                errorMessage = null
            )
        } catch (e: Exception) {
            Log.e("GoogleAuthClient", "Error authenticating with Firebase", e)
            SignInResult(
                userData = null,
                errorMessage = e.message
            )
        }


    }

    suspend fun signInWithEmailAndPassword(email: String, password: String): SignInResult {
        return try {
            val user = firebaseAuth.signInWithEmailAndPassword(email, password).await().user

            SignInResult(
                userData = user?.run {
                    UserData(
                        userId = uid,
                        username = displayName,
                        email = email,
                        profilePictureUrl = photoUrl.toString()
                    )
                },
                errorMessage = null
            )
        } catch (e: Exception) {
            SignInResult(
                userData = null,
                errorMessage = e.message
            )

        }
    }

    suspend fun createUserWithEmailAndPassword(email: String, password: String): SignInResult {
        return try {
            val user = firebaseAuth.createUserWithEmailAndPassword(email, password).await().user
            SignInResult(
                userData = user?.run {
                    UserData(
                        userId = uid,
                        username = displayName,
                        email = email,
                        profilePictureUrl = photoUrl.toString()
                    )
                },
                errorMessage = null
            )
        } catch (e: Exception) {
            SignInResult(
                userData = null,
                errorMessage = e.message
            )
        }
    }

    fun getCurrentUser(): UserData? = firebaseAuth.currentUser?.run {
        UserData(
            userId = uid,
            username = displayName,
            email = email,
            profilePictureUrl = photoUrl.toString()
        )
    }

    suspend fun signOut() {
        try {
            val state = ClearCredentialStateRequest()
            credentialManager.clearCredentialState(state)
            firebaseAuth.signOut()


        } catch (e: Exception) {
            Log.e("GoogleAuthClient", "Error signing out", e)
            throw e
        }
    }
}