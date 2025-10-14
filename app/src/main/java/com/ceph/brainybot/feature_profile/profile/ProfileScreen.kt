package com.ceph.brainybot.feature_profile.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.ColorLens
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ceph.brainybot.feature_auth.domain.model.UserData
import com.ceph.brainybot.feature_profile.model.ThemeMode
import com.ceph.brainybot.feature_chat.presentation.components.ProfileTopBar

@Composable
fun ProfileScreen(
    userData: UserData?,
    themeViewModel: ThemeViewModel,
    onNavigateToHomeScreen: () -> Unit,
    onSignOutClick: () -> Unit,
    currentTheme: ThemeMode
) {



    Scaffold(
        topBar = { ProfileTopBar(onNavigateToHomeScreen = onNavigateToHomeScreen) }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = userData?.profilePictureUrl,
                    contentDescription = "User image",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    text = userData?.username ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            ProfileRow(
                icon = Icons.Outlined.Email,
                title = "Email",
                subtitle = userData?.email ?: ""
            )

            ThemeSwitcherRow(
                currentTheme = currentTheme,
                onThemeChange = themeViewModel::setThemeMode
            )

            ProfileRow(
                icon = Icons.Outlined.Info,
                title = "About",
                subtitle = "BrainyBot AI Assistant v1.0"
            )

            ProfileRow(
                icon = Icons.AutoMirrored.Filled.ExitToApp,
                title = "Sign Out",
                modifier = Modifier.clickable { onSignOutClick() }
            )
        }
    }
}

@Composable
private fun ProfileRow(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    subtitle: String? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = title, modifier = Modifier.size(30.dp))
        Spacer(Modifier.width(16.dp))
        Column {
            Text(title, style = MaterialTheme.typography.titleMedium)
            subtitle?.let {
                Text(it, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun ThemeSwitcherRow(
    currentTheme: ThemeMode,
    onThemeChange: (ThemeMode) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.ColorLens,
                contentDescription = "Theme"
            )
            Spacer(Modifier.width(16.dp))

            Text(
                "Color Scheme",
                style = MaterialTheme.typography.titleMedium
            )
        }

        Box {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.clip(RoundedCornerShape(12.dp))
            ) {
                DropdownMenuItem(
                    text = { Text("System Default") },
                    onClick = {
                        onThemeChange(ThemeMode.SYSTEM)
                        expanded = false
                    },
                    trailingIcon = {
                        if (currentTheme == ThemeMode.SYSTEM) {
                            Icon(Icons.Default.Check, null)
                        }
                    }
                )
                DropdownMenuItem(
                    text = { Text("Light Theme") },
                    leadingIcon = { Icon(Icons.Default.WbSunny, null) },
                    onClick = {
                        onThemeChange(ThemeMode.LIGHT)
                        expanded = false
                    },
                    trailingIcon = {
                        if (currentTheme == ThemeMode.LIGHT) {
                            Icon(Icons.Default.Check, null)
                        }
                    }
                )
                DropdownMenuItem(
                    text = { Text("Dark Theme") },
                    leadingIcon = { Icon(Icons.Default.DarkMode, null) },
                    onClick = {
                        onThemeChange(ThemeMode.DARK)
                        expanded = false
                    },
                    trailingIcon = {
                        if (currentTheme == ThemeMode.DARK) {
                            Icon(Icons.Default.Check, null)
                        }
                    }
                )
            }
        }
    }
}
