package com.ceph.brainybot.app.presentation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ceph.brainybot.feature_auth.presentation.signIn.CreateUserScreen
import com.ceph.brainybot.feature_auth.presentation.signIn.SignInScreen
import com.ceph.brainybot.feature_auth.presentation.viewModel.AuthViewModel
import com.ceph.brainybot.feature_profile.model.ThemeMode
import com.ceph.brainybot.feature_chat.presentation.home.AnimationViewModel
import com.ceph.brainybot.feature_chat.presentation.home.ChatViewModel
import com.ceph.brainybot.feature_chat.presentation.home.HomeScreen
import com.ceph.brainybot.feature_profile.profile.ProfileScreen
import com.ceph.brainybot.feature_profile.profile.ThemeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun NavHostSetUp(
    apiViewModel: ChatViewModel,
    authViewModel: AuthViewModel,
    animationViewModel: AnimationViewModel,
    themeViewModel: ThemeViewModel,
    message: String,
    showMessageBar: Boolean,
    backgroundColor: Color,
    currentTheme: ThemeMode
) {

    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.SignInScreen
    ) {

        composable<NavRoutes.SignInScreen>(
            enterTransition = {
                slideInHorizontally(animationSpec = tween(300)) { it -> it } + fadeIn()
            },
            exitTransition = {
                slideOutHorizontally { it -> -it } + fadeOut()
            }
        ) {
            SignInScreen(
                viewModel = authViewModel,
                navigateToHome = {
                    navController.navigate(NavRoutes.HomeScreen) {
                        popUpTo(NavRoutes.SignInScreen) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                navigateToCreateUser = {
                    navController.navigate(NavRoutes.CreateUserScreen)
                }
            )
        }
        composable<NavRoutes.CreateUserScreen>(
            enterTransition = {
                slideInHorizontally(animationSpec = tween(300)) { it -> -it } + fadeIn()
            },
            exitTransition = {
                slideOutHorizontally { it -> it } + fadeOut()
            }
        ) {
            CreateUserScreen(
                viewModel = authViewModel,
                navigateToHome = {
                    navController.navigate(NavRoutes.HomeScreen) {
                        popUpTo(NavRoutes.CreateUserScreen) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                navigateToSignIn = {
                    navController.navigate(NavRoutes.SignInScreen)
                }
            )
        }
        composable<NavRoutes.HomeScreen>(
            enterTransition = {
                slideInHorizontally(animationSpec = tween(300)) { it -> -it } + fadeIn()
            },
            exitTransition = {
                slideOutHorizontally { it -> it } + fadeOut()
            }
        ) {
            HomeScreen(
                chatViewModel = apiViewModel,
                authViewModel = authViewModel,
                message = message,
                showMessageBar = showMessageBar,
                backgroundColor = backgroundColor,
                onNavigateToProfileScreen = {
                    navController.navigate(NavRoutes.ProfileScreen)
                },
                drawerState = drawerState,
                scope = scope,
                animationViewModel = animationViewModel
            )
        }

        composable<NavRoutes.ProfileScreen>(
            enterTransition = {
                slideInVertically(animationSpec = tween(300)) { it -> -it / 3 } + fadeIn()
            },
            exitTransition = {
                slideOutVertically { it -> -it / 3 } + fadeOut()
            }
        ) {
            ProfileScreen(
                userData = authViewModel.getCurrentUser(),
                onNavigateToHomeScreen = {
                    navController.navigate(NavRoutes.HomeScreen)
                },
                onSignOutClick = {
                    scope.launch {
                        drawerState.close()
                        delay(300)

                        authViewModel.signOut()

                        navController.navigate(NavRoutes.SignInScreen) {
                            popUpTo(NavRoutes.HomeScreen) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }

                },
                themeViewModel = themeViewModel,
                currentTheme = currentTheme
            )
        }

    }

}
