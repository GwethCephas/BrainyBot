package com.ceph.brainybot.app.presentation

import kotlinx.serialization.Serializable

sealed class NavRoutes() {
    @Serializable
    object SignInScreen : NavRoutes()
    @Serializable
    object CreateUserScreen : NavRoutes()
    @Serializable
    object HomeScreen : NavRoutes()
    @Serializable
    object ProfileScreen : NavRoutes()


}