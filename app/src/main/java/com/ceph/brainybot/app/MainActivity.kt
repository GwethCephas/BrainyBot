package com.ceph.brainybot.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.ceph.brainybot.app.ui.theme.BrainyBotTheme
import com.ceph.brainybot.app.domain.model.NetworkStatus
import com.ceph.brainybot.app.domain.repository.NetworkConnectivityObserver
import com.ceph.brainybot.app.presentation.NavHostSetUp
import com.ceph.brainybot.feature_auth.presentation.viewModel.AuthViewModel
import com.ceph.brainybot.feature_chat.data.firestoreChatManager.FirestoreChatManager
import com.ceph.brainybot.feature_chat.domain.repository.ChatRepository
import com.ceph.brainybot.feature_profile.model.ThemeMode
import com.ceph.brainybot.feature_chat.presentation.home.AnimationViewModel
import com.ceph.brainybot.feature_chat.presentation.home.ChatViewModel
import com.ceph.brainybot.feature_profile.profile.ThemeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.getValue

class MainActivity : ComponentActivity() {

    val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    val networkObserver: NetworkConnectivityObserver by
    inject { parametersOf(this, coroutineScope) }


    private val authViewModel: AuthViewModel by viewModel {
        parametersOf(this)
    }



    private val firestoreChatManager: FirestoreChatManager by inject {
        parametersOf(this)
    }
    private val chatRepository: ChatRepository by inject {
        parametersOf(firestoreChatManager)
    }
    private val chatViewModel: ChatViewModel by viewModel {
        parametersOf(chatRepository)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.light(
                Color.Transparent.hashCode(),
                Color.Transparent.hashCode()
            )
        )





        setContent {

            // Network observer
            val status by networkObserver.networkStatus.collectAsState()

            var message by rememberSaveable { mutableStateOf("") }
            var backgroundColor by remember { mutableStateOf(Color.Companion.Red) }
            var showMessage by rememberSaveable { mutableStateOf(false) }

            LaunchedEffect(key1 = status) {
                when (status) {
                    NetworkStatus.Connected -> {
                        message = "Back Online"
                        backgroundColor = Color.Companion.Green
                        delay(3000L)
                        showMessage = false
                    }

                    NetworkStatus.Disconnected -> {
                        message = "No Internet Connection"
                        backgroundColor = Color.Companion.Red
                        showMessage = true
                    }
                }
            }
            val animationViewModel = koinViewModel<AnimationViewModel>()

            val themeViewModel = koinViewModel<ThemeViewModel>()

            val themeMode by themeViewModel.themeMode.collectAsState(initial = ThemeMode.SYSTEM)

            BrainyBotTheme(
                themeMode = themeMode
            ) {


                NavHostSetUp(
                    authViewModel = authViewModel,
                    apiViewModel = chatViewModel,
                    themeViewModel = themeViewModel,
                    message = message,
                    showMessageBar = showMessage,
                    backgroundColor = backgroundColor,
                    animationViewModel = animationViewModel,
                    currentTheme = themeMode
                )

            }
        }
    }
}