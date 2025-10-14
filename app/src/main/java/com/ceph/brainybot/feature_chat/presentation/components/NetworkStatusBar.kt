package com.ceph.brainybot.feature_chat.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun NetworkStatusBar(
    modifier: Modifier = Modifier,
    message: String,
    showMessageBar: Boolean,
    backgroundColor: Color,
) {

    AnimatedVisibility(
        visible = showMessageBar,
        enter = slideInVertically { height -> height },
        exit = slideOutHorizontally { height -> height }
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(backgroundColor), contentAlignment = Alignment.Center
        ) {

            Text(
                text = message,
                color = Color.White,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                fontWeight = MaterialTheme.typography.bodyLarge.fontWeight
            )
        }
    }

}