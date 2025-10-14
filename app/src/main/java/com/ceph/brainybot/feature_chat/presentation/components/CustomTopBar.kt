package com.ceph.brainybot.feature_chat.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit,
    onCreateChatClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        modifier = modifier.padding(5.dp),
        title = {
            Text(
                text = "BrainyBot",
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        navigationIcon = {
            IconButton(
                onClick = {
                   onNavigationIconClick()
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.Sort,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

        },
        actions = {
            IconButton(
                onClick = {
                    onCreateChatClick()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircleOutline,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    )
}