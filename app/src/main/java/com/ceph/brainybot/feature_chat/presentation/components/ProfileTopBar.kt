package com.ceph.brainybot.feature_chat.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTopBar(
    onNavigateToHomeScreen: () -> Unit,
) {

    TopAppBar(
        title = {
            Text(
                text = "Settings",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.SemiBold
            )


        },
        navigationIcon = {
            IconButton(
                onClick = {
                    onNavigateToHomeScreen()
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Home Screen"
                )
            }
        }
    )

}