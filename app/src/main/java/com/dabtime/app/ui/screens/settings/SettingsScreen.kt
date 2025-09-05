package com.dabtime.app.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dabtime.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onSignOut: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.width(48.dp))
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // User Profile Section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Profile",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = DabGreen
                            )
                            
                            Spacer(modifier = Modifier.width(12.dp))
                            
                            Column {
                                Text(
                                    text = uiState.userName,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = if (uiState.isAnonymous) "Anonymous User" else uiState.userEmail,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }
            }
            
            // Timer Settings
            item {
                SettingsSection(
                    title = "Timer Settings",
                    items = listOf(
                        SettingsItem(
                            icon = Icons.Default.Timer,
                            title = "Default Timer Mode",
                            subtitle = uiState.defaultTimerMode,
                            onClick = { viewModel.showTimerModeDialog() }
                        ),
                        SettingsItem(
                            icon = Icons.Default.Vibration,
                            title = "Haptic Feedback",
                            subtitle = if (uiState.hapticFeedback) "Enabled" else "Disabled",
                            hasSwitch = true,
                            switchValue = uiState.hapticFeedback,
                            onSwitchChange = { viewModel.toggleHapticFeedback(it) }
                        ),
                        SettingsItem(
                            icon = Icons.Default.VolumeUp,
                            title = "Sound Effects",
                            subtitle = if (uiState.soundEffects) "Enabled" else "Disabled",
                            hasSwitch = true,
                            switchValue = uiState.soundEffects,
                            onSwitchChange = { viewModel.toggleSoundEffects(it) }
                        )
                    )
                )
            }
            
            // Social Settings
            item {
                SettingsSection(
                    title = "Social",
                    items = listOf(
                        SettingsItem(
                            icon = Icons.Default.Notifications,
                            title = "Friend Notifications",
                            subtitle = if (uiState.friendNotifications) "Enabled" else "Disabled",
                            hasSwitch = true,
                            switchValue = uiState.friendNotifications,
                            onSwitchChange = { viewModel.toggleFriendNotifications(it) }
                        ),
                        SettingsItem(
                            icon = Icons.Default.Share,
                            title = "Auto-share Sessions",
                            subtitle = if (uiState.autoShareSessions) "Enabled" else "Disabled",
                            hasSwitch = true,
                            switchValue = uiState.autoShareSessions,
                            onSwitchChange = { viewModel.toggleAutoShareSessions(it) }
                        )
                    )
                )
            }
            
            // App Settings
            item {
                SettingsSection(
                    title = "App",
                    items = listOf(
                        SettingsItem(
                            icon = Icons.Default.Palette,
                            title = "Theme",
                            subtitle = uiState.theme,
                            onClick = { viewModel.showThemeDialog() }
                        ),
                        SettingsItem(
                            icon = Icons.Default.Info,
                            title = "About",
                            subtitle = "Version ${uiState.appVersion}",
                            onClick = { viewModel.showAboutDialog() }
                        )
                    )
                )
            }
            
            // Account Actions
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Account",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Sign Out Button
                        OutlinedButton(
                            onClick = {
                                viewModel.signOut()
                                onSignOut()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = DabRed
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.ExitToApp,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Sign Out")
                        }
                        
                        if (!uiState.isAnonymous) {
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            // Delete Account Button
                            TextButton(
                                onClick = { viewModel.showDeleteAccountDialog() },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Delete Account",
                                    color = DabRed
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    items: List<SettingsItem>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            items.forEach { item ->
                SettingsItemRow(
                    item = item,
                    modifier = Modifier.fillMaxWidth()
                )
                
                if (item != items.last()) {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun SettingsItemRow(
    item: SettingsItem,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .then(
                if (item.onClick != null) {
                    Modifier.clickable { item.onClick.invoke() }
                } else Modifier
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = DabGreen
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            
            if (item.subtitle.isNotEmpty()) {
                Text(
                    text = item.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
        
        if (item.hasSwitch) {
            Switch(
                checked = item.switchValue,
                onCheckedChange = item.onSwitchChange ?: {},
                colors = SwitchDefaults.colors(
                    checkedThumbColor = DabGreen,
                    checkedTrackColor = DabGreen.copy(alpha = 0.5f)
                )
            )
        } else if (item.onClick != null) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

data class SettingsItem(
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val title: String,
    val subtitle: String = "",
    val hasSwitch: Boolean = false,
    val switchValue: Boolean = false,
    val onSwitchChange: ((Boolean) -> Unit)? = null,
    val onClick: (() -> Unit)? = null
)