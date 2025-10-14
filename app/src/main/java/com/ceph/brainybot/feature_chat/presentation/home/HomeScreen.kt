package com.ceph.brainybot.feature_chat.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.ceph.brainybot.feature_auth.presentation.viewModel.AuthViewModel
import com.ceph.brainybot.feature_chat.data.dto.Content
import com.ceph.brainybot.feature_chat.data.dto.GeminiRequestDto
import com.ceph.brainybot.feature_chat.data.dto.Part
import com.ceph.brainybot.feature_chat.presentation.components.AppDrawerSheet
import com.ceph.brainybot.feature_chat.presentation.components.CreateChat
import com.ceph.brainybot.feature_chat.presentation.components.CustomTopBar
import com.ceph.brainybot.feature_chat.presentation.components.NetworkStatusBar
import com.ceph.brainybot.feature_chat.presentation.components.TextComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    chatViewModel: ChatViewModel,
    authViewModel: AuthViewModel,
    message: String,
    showMessageBar: Boolean,
    backgroundColor: Color,
    onNavigateToProfileScreen: () -> Unit,
    drawerState: DrawerState,
    scope: CoroutineScope,
    animationViewModel: AnimationViewModel
) {

    val messages by chatViewModel.messages.collectAsState()
    val chats by chatViewModel.chatRooms.collectAsState()

    var userRequest by remember { mutableStateOf("") }
    var chatRoomSearch by remember { mutableStateOf("") }

    val uiState by chatViewModel.uiState.collectAsState()

    val focusManager = LocalFocusManager.current

    val currentUserId = authViewModel.getCurrentUser()?.userId.toString()
    val currentRoomId by chatViewModel.currentRoomId.collectAsState()



    LaunchedEffect(key1 = messages) {
        chatViewModel.observeMessages(currentRoomId.toString())
    }

    LaunchedEffect(key1 =currentUserId) {
        chatViewModel.observeChatRooms()
    }





    ModalNavigationDrawer(
        drawerState = drawerState,
        scrimColor = DrawerDefaults.scrimColor,
        drawerContent = {
            AppDrawerSheet(
                chatRoomSearchText = chatRoomSearch,
                onChatRoomSearch = {
                    chatRoomSearch = it
                },
                drawerState = drawerState,
                scope = scope,
                chats = chats,
                currentRoomId = currentRoomId,
                onChatSelected = { newRoomId ->
                    chatViewModel.setCurrentRoomId(newRoomId)
                },
                onDelete = { roomId ->
                    chatViewModel.deleteChatRoom(roomId)
                },
                onNavigateToProfileScreen = onNavigateToProfileScreen,
                userData = authViewModel.getCurrentUser()
            )
        }
    ) {

        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing),
            topBar = {
                CustomTopBar(onNavigationIconClick = {
                    scope.launch {
                        drawerState.open()
                    }
                }, onCreateChatClick = {
                    chatViewModel.setCurrentRoomId(null)
                })

            },
            floatingActionButton = {

                AnimatedVisibility(
                    visible = currentRoomId == null, enter = slideInVertically(
                        animationSpec = spring(
                            dampingRatio = 0.5f, stiffness = 100f
                        )
                    ) + fadeIn(animationSpec = tween(300)), exit = slideOutVertically(
                        animationSpec = spring(
                            dampingRatio = 0.5f, stiffness = 100f
                        )
                    ) + fadeOut(animationSpec = tween(300))

                ) {
                    CreateChat(
                        viewModel = chatViewModel, modifier = modifier
                    )
                }

            },
            bottomBar = {
                NetworkStatusBar(
                    modifier = modifier,
                    message = message,
                    showMessageBar = showMessageBar,
                    backgroundColor = backgroundColor
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->


            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (messages.isEmpty()) {
                    Column(
                        modifier = modifier.weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.MenuBook,
                            contentDescription = null,
                            modifier = Modifier.size(200.dp),
                            tint = Color.LightGray
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = if (currentRoomId == null) {
                                "Hello there, I am BrainyBot!! Please create \n " + " a chat to start the conversation."
                            } else "You have created a new chat  with Brainybot \n" + "what can i help you with?",
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(
                            items = messages,
                            key = { message -> "${currentRoomId}_${message.timestamp}" }
                        ) { message ->
                            val isUserMessage = message.senderId == currentUserId
                            val messageId = message.timestamp.toString()

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                horizontalArrangement = if (message.senderId == currentUserId) Arrangement.End
                                else Arrangement.Start
                            ) {
                                TextComponent(
                                    text = message.text,
                                    containerColor = if (message.senderId == currentUserId) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onPrimary,
                                    contentColor = if (message.senderId == currentUserId) MaterialTheme.colorScheme.onPrimary
                                    else MaterialTheme.colorScheme.onBackground,
                                    messageId = messageId,
                                    isUserMessage = isUserMessage,
                                    typingSpeed = 20L,
                                    viewModel = animationViewModel
                                )
                            }


                        }
                    }
                }

                AnimatedVisibility(
                    visible = currentRoomId != null,
                    enter = slideInVertically(
                        animationSpec = spring(
                            dampingRatio = 0.5f, stiffness = 100f
                        )
                    ) + fadeIn(animationSpec = tween(300)),
                    exit = slideOutVertically(
                        animationSpec = spring(
                            dampingRatio = 0.5f, stiffness = 100f
                        )
                    ) + fadeOut(animationSpec = tween(300))

                ) {


                    OutlinedTextField(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp, horizontal = 20.dp),
                        label = { Text(text = "Enter your request") },
                        value = userRequest,
                        onValueChange = {
                            userRequest = it
                        },
                        trailingIcon = {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(30.dp), strokeWidth = 3.dp
                                )
                            } else {
                                Button(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .padding(5.dp),
                                    onClick = {
                                        if (userRequest.isNotEmpty() && currentRoomId != null) {
                                            val apiRequest = GeminiRequestDto(
                                                contents = listOf(
                                                    Content(
                                                        parts = listOf(
                                                            Part(
                                                                userRequest
                                                            )
                                                        )
                                                    )
                                                )
                                            )
                                            chatViewModel.generateResponse(
                                                request = apiRequest,
                                                roomId = currentRoomId!!,
                                                userRequest = userRequest,
                                                currentUser = currentUserId
                                            )
                                        }
                                        focusManager.clearFocus()

                                        userRequest = ""
                                    },
                                    shape = CircleShape,
                                    contentPadding = PaddingValues(0.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (userRequest.isEmpty()) MaterialTheme.colorScheme.onSurface
                                        else MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                ) {

                                    Icon(
                                        imageVector = Icons.AutoMirrored.Default.ArrowForward,
                                        contentDescription = null
                                    )
                                }
                            }

                        },
                        shape = RoundedCornerShape(30.dp),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                        colors = TextFieldDefaults.colors(
                            cursorColor = MaterialTheme.colorScheme.primary,
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.onPrimary,
                            disabledIndicatorColor = Color.Transparent,
                            focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                            unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }

            }


        }


    }
}

