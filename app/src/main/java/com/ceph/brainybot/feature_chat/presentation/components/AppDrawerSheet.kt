package com.ceph.brainybot.feature_chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.sharp.Clear
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ceph.brainybot.feature_auth.domain.model.UserData
import com.ceph.brainybot.feature_chat.domain.model.ChatRoom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AppDrawerSheet(
    chatRoomSearchText: String,
    onChatRoomSearch: (String) -> Unit,
    scope: CoroutineScope,
    drawerState: DrawerState,
    onNavigateToProfileScreen: () -> Unit,
    chats: List<ChatRoom>,
    currentRoomId: String?,
    onChatSelected: (String) -> Unit,
    onDelete: (String) -> Unit,
    userData: UserData?
) {

    val focusManager = LocalFocusManager.current
    val filteredChats = chats
        .filter { it.name.contains(chatRoomSearchText, ignoreCase = true) }
        .sortedByDescending { it.createdAt }


    ModalDrawerSheet(
        modifier = Modifier.fillMaxWidth(0.85f),
        drawerContainerColor = MaterialTheme.colorScheme.background
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            //  Search Field
            OutlinedTextField(
                value = chatRoomSearchText,
                onValueChange = onChatRoomSearch,
                shape = RoundedCornerShape(20.dp),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = "Search for previous chats"
                    )
                },
                trailingIcon = {
                    if (chatRoomSearchText.isNotEmpty()) {
                        IconButton(
                            modifier = Modifier.size(40.dp),
                            onClick = {
                                focusManager.clearFocus()
                                onChatRoomSearch("")
                            },
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = MaterialTheme.colorScheme.onBackground,
                                containerColor = MaterialTheme.colorScheme.background
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Sharp.Clear,
                                contentDescription = "Search for previous chats"
                            )
                        }
                    }
                },
                colors = TextFieldDefaults.colors(
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.onPrimary,
                    disabledIndicatorColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary
                ),
                label = { Text(text = "Search") },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    autoCorrectEnabled = true
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                modifier = Modifier.padding(10.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                modifier = Modifier.padding(10.dp),
                text = "Chats",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontWeight = FontWeight.SemiBold
            )


            val list = filteredChats.ifEmpty { chats }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 10.dp)
            ) {
                items(list) { chatRoom ->
                    var isExpanded by remember { mutableStateOf(false) }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp, horizontal = 10.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (chatRoom.roomId == currentRoomId)
                                    MaterialTheme.colorScheme.secondaryContainer
                                else
                                    Color.Transparent
                            )
                            .combinedClickable(
                                onClick = {
                                    scope.launch {
                                        drawerState.close()
                                        delay(300)
                                        chatRoom.roomId?.let { onChatSelected(it) }
                                    }
                                },
                                onLongClick = {
                                    isExpanded = !isExpanded
                                }
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            modifier = Modifier.padding(10.dp),
                            text = chatRoom.name,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        )
                        Box {
                            DropdownMenu(
                                expanded = isExpanded,
                                onDismissRequest = { isExpanded = false },
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.onSecondary)
                                    .padding(5.dp)
                                    .wrapContentSize()
                                    .clip(RoundedCornerShape(10.dp))
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Delete Chat") },
                                    onClick = {
                                        chatRoom.roomId?.let { onDelete(it) }
                                    }
                                )
                            }

                        }
                    }
                    HorizontalDivider(
                        color = Color.LightGray.copy(alpha = 0.4f),
                        thickness = 1.dp,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }
            }



            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .background(Color.Transparent)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onNavigateToProfileScreen() },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (userData?.profilePictureUrl != null) {
                    AsyncImage(
                        model = userData.profilePictureUrl,
                        contentDescription = "User profile image",
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile Screen",
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
                        modifier = Modifier
                            .size(30.dp)
                    )
                }

                Text(
                    text = userData?.username ?: "",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = FontWeight.SemiBold
                )
                IconButton(
                    onClick = {
                        onNavigateToProfileScreen()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Profile Screen",
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }

}



