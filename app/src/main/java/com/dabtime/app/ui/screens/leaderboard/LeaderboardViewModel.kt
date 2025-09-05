package com.dabtime.app.ui.screens.leaderboard

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
class LeaderboardViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(LeaderboardUiState())
    val uiState: StateFlow<LeaderboardUiState> = _uiState.asStateFlow()
    
    init {
        loadLeaderboard()
    }
    
    private fun loadLeaderboard() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                // Mock data for now - in real app, this would come from Firestore
                val mockEntries = listOf(
                    LeaderboardEntry(1, "DabMaster420", 2850, false),
                    LeaderboardEntry(2, "PrecisionPro", 2720, false),
                    LeaderboardEntry(3, "HeatSeeker", 2650, false),
                    LeaderboardEntry(4, "You", 2580, true),
                    LeaderboardEntry(5, "CloudChaser", 2450, false),
                    LeaderboardEntry(6, "TerpHunter", 2380, false),
                    LeaderboardEntry(7, "DabNinja", 2320, false),
                    LeaderboardEntry(8, "ConcentrateKing", 2280, false),
                    LeaderboardEntry(9, "VaporVibe", 2150, false),
                    LeaderboardEntry(10, "RigRider", 2100, false)
                )
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    leaderboardEntries = mockEntries,
                    userRank = 4,
                    userScore = 2580
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to load leaderboard"
                )
            }
        }
    }
    
    fun refreshLeaderboard() {
        loadLeaderboard()
    }
}

data class LeaderboardUiState(
    val isLoading: Boolean = false,
    val leaderboardEntries: List<LeaderboardEntry> = emptyList(),
    val userRank: Int = 0,
    val userScore: Int = 0,
    val errorMessage: String? = null
)

data class LeaderboardEntry(
    val rank: Int,
    val name: String,
    val score: Int,
    val isCurrentUser: Boolean
)