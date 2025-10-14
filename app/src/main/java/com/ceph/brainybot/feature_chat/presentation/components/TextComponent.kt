package com.ceph.brainybot.feature_chat.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ceph.brainybot.feature_chat.presentation.home.AnimationViewModel
import kotlinx.coroutines.delay

@Composable
fun TextComponent(
    modifier: Modifier = Modifier,
    text: String,
    containerColor: Color,
    contentColor: Color,
    messageId: String,
    isUserMessage: Boolean,
    typingSpeed: Long = 20L,
    viewModel : AnimationViewModel
) {
    val alreadyAnimatedMessage = viewModel.alreadyAnimatedMessage.collectAsState().value.contains(messageId)

    var animatedText by  remember {
        mutableStateOf(
            if (alreadyAnimatedMessage || isUserMessage) text
            else ""
        )
    }

    LaunchedEffect(messageId, alreadyAnimatedMessage,isUserMessage) {
        if (!isUserMessage && !alreadyAnimatedMessage) {
            for (i in text.indices) {
                animatedText = text.take(i + 1)
                delay(typingSpeed)
            }
            viewModel.markMessageAsAnimated(messageId)
        }
    }

    Card(
        modifier = modifier
            .wrapContentSize()
            .padding(10.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        Text(
            text = animatedText.replace("*", ""),
            modifier = Modifier.padding(10.dp),
            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
            color = contentColor
        )
    }
}
