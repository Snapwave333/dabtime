package com.dabtime.app.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dabtime.app.data.repository.AuthRepository
import com.dabtime.app.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()
    
    init {
        loadUserSettings()
    }
    
    private fun loadUserSettings() {
        viewModelScope.launch {
            try {
                val currentUser = authRepository.getCurrentUser()
                
                _uiState.value = _uiState.value.copy(
                    userName = currentUser?.displayName ?: "Anonymous User",
                    userEmail = currentUser?.email ?: "",
                    isAnonymous = currentUser?.isAnonymous ?: true,
                    // Load user preferences from Firestore
                    defaultTimerMode = "Balanced",
                    hapticFeedback = true,
                    soundEffects = true,
                    friendNotifications = true,
                    autoShareSessions = false,
                    theme = "Auto",
                    appVersion = "1.0.0"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Failed to load settings"
                )
            }
        }
    }
    
    fun toggleHapticFeedback(enabled: Boolean) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(hapticFeedback = enabled)
                // Update in Firestore
                userRepository.updateUserSettings("hapticFeedback", enabled)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    hapticFeedback = !enabled, // Revert on error
                    errorMessage = e.message ?: "Failed to update haptic feedback setting"
                )
            }
        }
    }

    fun toggleSoundEffects(enabled: Boolean) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(soundEffects = enabled)
                // Update in Firestore
                userRepository.updateUserSettings("soundEffects", enabled)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    soundEffects = !enabled, // Revert on error
                    errorMessage = e.message ?: "Failed to update sound effects setting"
                )
            }
        }
    }

    fun toggleFriendNotifications(enabled: Boolean) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(friendNotifications = enabled)
                // Update in Firestore
                userRepository.updateUserSettings("friendNotifications", enabled)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    friendNotifications = !enabled, // Revert on error
                    errorMessage = e.message ?: "Failed to update notification setting"
                )
            }
        }
    }

    fun toggleAutoShareSessions(enabled: Boolean) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(autoShareSessions = enabled)
                // Update in Firestore
                userRepository.updateUserSettings("autoShareSessions", enabled)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    autoShareSessions = !enabled, // Revert on error
                    errorMessage = e.message ?: "Failed to update auto-share setting"
                )
            }
        }
    }
    
    fun showTimerModeDialog() {
        _uiState.value = _uiState.value.copy(showTimerModeDialog = true)
    }
    
    fun hideTimerModeDialog() {
        _uiState.value = _uiState.value.copy(showTimerModeDialog = false)
    }
    
    fun setDefaultTimerMode(mode: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    defaultTimerMode = mode,
                    showTimerModeDialog = false
                )
                // Update in Firestore
                userRepository.updateUserSettings("defaultTimerMode", mode)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    showTimerModeDialog = false,
                    errorMessage = e.message ?: "Failed to update timer mode"
                )
            }
        }
    }
    
    fun showThemeDialog() {
        _uiState.value = _uiState.value.copy(showThemeDialog = true)
    }
    
    fun hideThemeDialog() {
        _uiState.value = _uiState.value.copy(showThemeDialog = false)
    }
    
    fun setTheme(theme: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    theme = theme,
                    showThemeDialog = false
                )
                // Update in Firestore
                userRepository.updateUserSettings("theme", theme)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    showThemeDialog = false,
                    errorMessage = e.message ?: "Failed to update theme"
                )
            }
        }
    }
    
    fun showAboutDialog() {
        _uiState.value = _uiState.value.copy(showAboutDialog = true)
    }
    
    fun hideAboutDialog() {
        _uiState.value = _uiState.value.copy(showAboutDialog = false)
    }
    
    fun showDeleteAccountDialog() {
        _uiState.value = _uiState.value.copy(showDeleteAccountDialog = true)
    }
    
    fun hideDeleteAccountDialog() {
        _uiState.value = _uiState.value.copy(showDeleteAccountDialog = false)
    }
    
    fun deleteAccount() {
        viewModelScope.launch {
            try {
                val result = authRepository.deleteAccount()
                if (result.isSuccess) {
                    _uiState.value = _uiState.value.copy(
                        showDeleteAccountDialog = false,
                        accountDeleted = true
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = result.exceptionOrNull()?.message ?: "Failed to delete account"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Failed to delete account"
                )
            }
        }
    }
    
    fun signOut() {
        viewModelScope.launch {
            try {
                authRepository.signOut()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Failed to sign out"
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

data class SettingsUiState(
    val userName: String = "",
    val userEmail: String = "",
    val isAnonymous: Boolean = true,
    val defaultTimerMode: String = "Balanced",
    val hapticFeedback: Boolean = true,
    val soundEffects: Boolean = true,
    val friendNotifications: Boolean = true,
    val autoShareSessions: Boolean = false,
    val theme: String = "Auto",
    val appVersion: String = "1.0.0",
    val showTimerModeDialog: Boolean = false,
    val showThemeDialog: Boolean = false,
    val showAboutDialog: Boolean = false,
    val showDeleteAccountDialog: Boolean = false,
    val accountDeleted: Boolean = false,
    val errorMessage: String? = null
)