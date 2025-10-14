package com.ceph.brainybot.feature_chat.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ceph.brainybot.feature_chat.presentation.home.ChatViewModel
import java.util.UUID

@Composable
fun CreateChat(
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel,
) {

    var isExpanded by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }

    val floatingActionButtonPosition = remember { mutableStateOf(IntOffset.Zero) }
    val buttonSize = remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.BottomEnd
    ) {

        Row(
            modifier = modifier.padding(bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            AnimatedVisibility(
                visible = isExpanded,
                enter = slideInHorizontally(
                    initialOffsetX = { fullWidth ->
                        floatingActionButtonPosition.value.x + buttonSize.value.width / 2
                    },
                    animationSpec =
                        spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                ) + fadeIn(),
                exit = slideOutHorizontally(
                    targetOffsetX = { fullWidth ->
                        floatingActionButtonPosition.value.x + buttonSize.value.width / 2
                    },
                ) + fadeOut(),
                modifier = modifier
            ) {
                TextField(
                    value = text,
                    onValueChange = {
                        text = it
                    },
                    shape = RoundedCornerShape(20.dp),
                    label = {
                        Text(text = "Create a Chat")
                    },
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 18.sp
                    ),
                    modifier = Modifier
                        .heightIn(min = 45.dp, max = 55.dp)
                        .widthIn(min = 150.dp, max = 200.dp),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            viewModel.createChat(
                                roomId = UUID.randomUUID().toString(),
                                name = text
                            )
                            text = ""
                            isExpanded = !isExpanded
                        }
                    ),
                    colors = TextFieldDefaults.colors(
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSecondary,
                        disabledIndicatorColor = Color.Transparent,
                        focusedContainerColor = MaterialTheme.colorScheme.onSecondary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.onSecondary,
                    ),
                )

            }
            FloatingActionButton(
                modifier = modifier
                    .onGloballyPositioned { coordinates ->
                        val position = coordinates.positionInParent()

                        buttonSize.value = coordinates.size
                        floatingActionButtonPosition.value = IntOffset(
                            position.x.toInt(),
                            position.y.toInt()
                        )

                    },
                onClick = {
                    isExpanded = !isExpanded
                },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {

                Icon(
                    imageVector = if (isExpanded) Icons.Default.Close
                    else Icons.Default.ChatBubble,
                    contentDescription = "Create New Chat"
                )
            }
        }
    }

}