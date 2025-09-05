package com.dabtime.app.ui.screens.social

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dabtime.app.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SocialViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SocialUiState())
    val uiState: StateFlow<SocialUiState> = _uiState.asStateFlow()
    
    init {
        loadFriends()
    }
    
    private fun loadFriends() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                // Mock friend data - in real app, this would come from Firestore
                val friends = listOf(
                    Friend(
                        id = "friend1",
                        name = "DabMaster420",
                        isOnline = true,
                        isDabbing = true,
                        lastSeen = "now"
                    ),
                    Friend(
                        id = "friend2",
                        name = "CloudChaser",
                        isOnline = true,
                        isDabbing = false,
                        lastSeen = "now"
                    ),
                    Friend(
                        id = "friend3",
                        name = "TerpHunter",
                        isOnline = false,
                        isDabbing = false,
                        lastSeen = "2 hours ago"
                    ),
                    Friend(
                        id = "friend4",
                        name = "VaporVibe",
                        isOnline = false,
                        isDabbing = false,
                        lastSeen = "yesterday"
                    )
                )
                
                val activeFriends = friends.count { it.isOnline }
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    friends = friends,
                    activeFriends = activeFriends
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to load friends"
                )
            }
        }
    }
    
    fun showAddFriendDialog() {
        _uiState.value = _uiState.value.copy(showAddFriendDialog = true)
    }
    
    fun hideAddFriendDialog() {
        _uiState.value = _uiState.value.copy(showAddFriendDialog = false)
    }
    
    fun addFriend(friendCode: String) {
        viewModelScope.launch {
            try {
                // In real app, this would add friend via Firestore
                hideAddFriendDialog()
                loadFriends() // Refresh the list
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Failed to add friend"
                )
            }
        }
    }
    
    fun inviteFriendToDab(friendId: String) {
        viewModelScope.launch {
            try {
                // In real app, this would send notification via Firebase
                _uiState.value = _uiState.value.copy(
                    successMessage = "Dab invitation sent!"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Failed to send invitation"
                )
            }
        }
    }
    
    fun toggleSessionSharing(enabled: Boolean) {
        viewModelScope.launch {
            try {
                // In real app, this would update user preferences in Firestore
                _uiState.value = _uiState.value.copy(
                    shareSessionsByDefault = enabled
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Failed to update settings"
                )
            }
        }
    }
    
    fun setUserDabbingStatus(isDabbing: Boolean) {
        viewModelScope.launch {
            try {
                // In real app, this would update status in Realtime Database
                _uiState.value = _uiState.value.copy(
                    isUserDabbing = isDabbing
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Failed to update status"
                )
            }
        }
    }
    
    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null,
            successMessage = null
        )
    }
}

data class SocialUiState(
    val isLoading: Boolean = false,
    val friends: List<Friend> = emptyList(),
    val activeFriends: Int = 0,
    val isUserDabbing: Boolean = false,
    val shareSessionsByDefault: Boolean = false,
    val showAddFriendDialog: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

data class Friend(
    val id: String,
    val name: String,
    val isOnline: Boolean,
    val isDabbing: Boolean,
    val lastSeen: String,
    val profileImageUrl: String? = null
)