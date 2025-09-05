package com.dabtime.app.ui.screens.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dabtime.app.data.model.HeatZone
import com.dabtime.app.data.model.TimerMode
import com.dabtime.app.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    
    private var timerJob: Job? = null
    
    private val _uiState = MutableStateFlow(TimerUiState())
    val uiState: StateFlow<TimerUiState> = _uiState.asStateFlow()
    
    fun toggleTimer() {
        if (_uiState.value.isRunning) {
            pauseTimer()
        } else {
            startTimer()
        }
    }
    
    fun resetTimer() {
        timerJob?.cancel()
        val currentState = _uiState.value
        _uiState.value = currentState.copy(
            isRunning = false,
            timeRemaining = currentState.totalTime,
            currentHeatZone = calculateHeatZone(currentState.totalTime, currentState.timerMode)
        )
    }
    
    fun setCustomTime(seconds: Int) {
        val newTime = seconds.coerceAtLeast(1)
        val currentState = _uiState.value
        _uiState.value = currentState.copy(
            timeRemaining = newTime,
            totalTime = newTime,
            currentHeatZone = calculateHeatZone(newTime, currentState.timerMode),
            isRunning = false
        )
        timerJob?.cancel()
    }
    
    fun setTimerMode(mode: TimerMode) {
        val currentState = _uiState.value
        val newTotalTime = getDefaultTimeForMode(mode)
        _uiState.value = currentState.copy(
            timerMode = mode,
            totalTime = newTotalTime,
            timeRemaining = newTotalTime,
            currentHeatZone = calculateHeatZone(newTotalTime, mode),
            isRunning = false
        )
        timerJob?.cancel()
    }
    
    private fun startTimer() {
        _uiState.value = _uiState.value.copy(isRunning = true)
        timerJob = viewModelScope.launch {
            while (_uiState.value.isRunning) {
                delay(1000)
                val currentState = _uiState.value
                val newTime = currentState.timeRemaining - 1
                val newHeatZone = calculateHeatZone(newTime, currentState.timerMode)
                
                // Check for heat zone transition for haptic feedback
                val heatZoneChanged = newHeatZone != currentState.currentHeatZone
                
                _uiState.value = currentState.copy(
                    timeRemaining = newTime,
                    currentHeatZone = newHeatZone,
                    heatZoneChanged = heatZoneChanged
                )
                
                // Reset heat zone change flag after a brief moment
                if (heatZoneChanged) {
                    delay(100)
                    _uiState.value = _uiState.value.copy(heatZoneChanged = false)
                }
            }
        }
    }
    
    private fun pauseTimer() {
        _uiState.value = _uiState.value.copy(isRunning = false)
        timerJob?.cancel()
    }
    
    private fun calculateHeatZone(timeRemaining: Int, mode: TimerMode): HeatZone {
        val thresholds = getHeatZoneThresholds(mode)
        
        return when {
            timeRemaining < 0 -> HeatZone.NOT_READY // Overtime - could be a different state
            timeRemaining >= thresholds.highTemp -> HeatZone.HIGH_TEMP
            timeRemaining >= thresholds.mediumTemp -> HeatZone.MEDIUM_TEMP
            timeRemaining >= thresholds.lowTemp -> HeatZone.LOW_TEMP
            else -> HeatZone.NOT_READY
        }
    }
    
    private fun getHeatZoneThresholds(mode: TimerMode): HeatZoneThresholds {
        return when (mode) {
            TimerMode.STRICT -> HeatZoneThresholds(
                highTemp = 55,
                mediumTemp = 40,
                lowTemp = 20
            )
            TimerMode.BALANCED -> HeatZoneThresholds(
                highTemp = 60,
                mediumTemp = 45,
                lowTemp = 25
            )
            TimerMode.CHILL -> HeatZoneThresholds(
                highTemp = 70,
                mediumTemp = 50,
                lowTemp = 30
            )
        }
    }
    
    private fun getDefaultTimeForMode(mode: TimerMode): Int {
        return when (mode) {
            TimerMode.STRICT -> 75 // 1:15
            TimerMode.BALANCED -> 90 // 1:30
            TimerMode.CHILL -> 120 // 2:00
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}

data class TimerUiState(
    val timeRemaining: Int = 90,
    val totalTime: Int = 90,
    val currentHeatZone: HeatZone = HeatZone.HIGH_TEMP,
    val timerMode: TimerMode = TimerMode.BALANCED,
    val isRunning: Boolean = false,
    val heatZoneChanged: Boolean = false,
    val errorMessage: String? = null
)

data class HeatZoneThresholds(
    val highTemp: Int,
    val mediumTemp: Int,
    val lowTemp: Int
)