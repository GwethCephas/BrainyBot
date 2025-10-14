package com.ceph.brainybot.feature_chat.presentation.components

import android.util.Patterns

fun isEmailValid(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isPasswordValid(password: String): Boolean {
    val passwordRegex =  Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}\$")
    return passwordRegex.matches(password)

}