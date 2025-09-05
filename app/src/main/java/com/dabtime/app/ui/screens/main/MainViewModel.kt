package com.dabtime.app.ui.screens.main

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
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()
    
    init {
        loadUserData()
    }
    
    private fun loadUserData() {
        viewModelScope.launch {
            val currentUser = authRepository.getCurrentUser()
            val welcomeMessage = if (currentUser?.isAnonymous == true) {
                "Welcome, Anonymous Dabber!"
            } else {
                "Welcome back, ${currentUser?.displayName ?: "Dabber"}!"
            }
            
            // Load user stats (placeholder data for now)
            _uiState.value = _uiState.value.copy(
                welcomeMessage = welcomeMessage,
                totalDabs = 42, // This would come from Firestore
                weeklyRank = 15, // This would come from leaderboard
                badgesEarned = 3, // This would come from achievements
                recentDabs = listOf(
                    "Perfect dab at 2:30 PM - Medium temp",
                    "Quick session at 11:45 AM - Low temp",
                    "Social dab with friends at 9:20 PM - High temp"
                )
            )
        }
    }
    
    fun startQuickDab() {
        viewModelScope.launch {
            // Start a quick dab session with default settings
            // This would integrate with the timer functionality
            _uiState.value = _uiState.value.copy(
                isQuickDabActive = true
            )
        }
    }
    
    fun refreshData() {
        loadUserData()
    }
}

data class MainUiState(
    val welcomeMessage: String = "Welcome to Dab Time!",
    val totalDabs: Int = 0,
    val weeklyRank: Int = 0,
    val badgesEarned: Int = 0,
    val recentDabs: List<String> = emptyList(),
    val isQuickDabActive: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)