package com.dabtime.app.ui.screens.achievements

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
class AchievementsViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AchievementsUiState())
    val uiState: StateFlow<AchievementsUiState> = _uiState.asStateFlow()
    
    init {
        loadBadges()
    }
    
    private fun loadBadges() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                // Mock badge data - in real app, this would come from Firestore
                val badges = listOf(
                    Badge(
                        id = "midnight_dabber",
                        name = "Midnight Dabber",
                        description = "Dab between 12-3 AM",
                        icon = "🌙",
                        isEarned = true,
                        earnedDate = System.currentTimeMillis() - 86400000 // Yesterday
                    ),
                    Badge(
                        id = "early_riser",
                        name = "Early Riser",
                        description = "Dab before 7 AM",
                        icon = "🌅",
                        isEarned = false
                    ),
                    Badge(
                        id = "social_dabber",
                        name = "Social Dabber",
                        description = "Share 10 sessions with friends",
                        icon = "👥",
                        isEarned = true,
                        earnedDate = System.currentTimeMillis() - 172800000 // 2 days ago
                    ),
                    Badge(
                        id = "precision_master",
                        name = "Precision Master",
                        description = "Hit perfect temp 50 times",
                        icon = "🎯",
                        isEarned = false
                    ),
                    Badge(
                        id = "heat_seeker",
                        name = "Heat Seeker",
                        description = "Use all heat zones in one day",
                        icon = "🔥",
                        isEarned = true,
                        earnedDate = System.currentTimeMillis() - 259200000 // 3 days ago
                    ),
                    Badge(
                        id = "consistency_king",
                        name = "Consistency King",
                        description = "Dab daily for 7 days",
                        icon = "👑",
                        isEarned = false
                    ),
                    Badge(
                        id = "weekend_warrior",
                        name = "Weekend Warrior",
                        description = "Extra active on weekends",
                        icon = "⚔️",
                        isEarned = false
                    ),
                    Badge(
                        id = "temperature_guru",
                        name = "Temperature Guru",
                        description = "Master all timer modes",
                        icon = "🧘",
                        isEarned = false
                    )
                )
                
                val earnedCount = badges.count { it.isEarned }
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    badges = badges,
                    earnedBadges = earnedCount,
                    totalBadges = badges.size
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to load badges"
                )
            }
        }
    }
    
    fun selectBadge(badge: Badge) {
        _uiState.value = _uiState.value.copy(selectedBadge = badge)
    }
    
    fun clearSelectedBadge() {
        _uiState.value = _uiState.value.copy(selectedBadge = null)
    }
    
    fun refreshBadges() {
        loadBadges()
    }
}

data class AchievementsUiState(
    val isLoading: Boolean = false,
    val badges: List<Badge> = emptyList(),
    val earnedBadges: Int = 0,
    val totalBadges: Int = 0,
    val selectedBadge: Badge? = null,
    val errorMessage: String? = null
)

data class Badge(
    val id: String,
    val name: String,
    val description: String,
    val icon: String,
    val isEarned: Boolean = false,
    val earnedDate: Long? = null,
    val progress: Int = 0,
    val maxProgress: Int = 1
)