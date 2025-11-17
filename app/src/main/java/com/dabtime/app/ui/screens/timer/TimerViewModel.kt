package com.dabtime.app.ui.screens.timer

import androidx.lifecycle.SavedStateHandle
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
import kotlinx.coroutines.CancellationException

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val KEY_TIME_REMAINING = "timeRemaining"
        private const val KEY_TOTAL_TIME = "totalTime"
        private const val KEY_IS_RUNNING = "isRunning"
        private const val KEY_TIMER_MODE = "timerMode"
    }

    private var timerJob: Job? = null

    private val _uiState = MutableStateFlow(
        TimerUiState(
            timeRemaining = savedStateHandle.get<Int>(KEY_TIME_REMAINING) ?: 90,
            totalTime = savedStateHandle.get<Int>(KEY_TOTAL_TIME) ?: 90,
            isRunning = false, // Never auto-resume on config change
            timerMode = savedStateHandle.get<TimerMode>(KEY_TIMER_MODE) ?: TimerMode.BALANCED
        )
    )
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
            currentHeatZone = calculateHeatZone(currentState.totalTime, currentState.timerMode),
            errorMessage = null
        )
        savedStateHandle[KEY_TIME_REMAINING] = currentState.totalTime
        savedStateHandle[KEY_IS_RUNNING] = false
    }

    fun setCustomTime(seconds: Int) {
        val newTime = seconds.coerceAtLeast(1)
        val currentState = _uiState.value
        _uiState.value = currentState.copy(
            timeRemaining = newTime,
            totalTime = newTime,
            currentHeatZone = calculateHeatZone(newTime, currentState.timerMode),
            isRunning = false,
            errorMessage = null
        )
        timerJob?.cancel()
        savedStateHandle[KEY_TIME_REMAINING] = newTime
        savedStateHandle[KEY_TOTAL_TIME] = newTime
        savedStateHandle[KEY_IS_RUNNING] = false
    }

    fun setTimerMode(mode: TimerMode) {
        val currentState = _uiState.value
        val newTotalTime = getDefaultTimeForMode(mode)
        _uiState.value = currentState.copy(
            timerMode = mode,
            totalTime = newTotalTime,
            timeRemaining = newTotalTime,
            currentHeatZone = calculateHeatZone(newTotalTime, mode),
            isRunning = false,
            errorMessage = null
        )
        timerJob?.cancel()
        savedStateHandle[KEY_TIMER_MODE] = mode
        savedStateHandle[KEY_TOTAL_TIME] = newTotalTime
        savedStateHandle[KEY_TIME_REMAINING] = newTotalTime
        savedStateHandle[KEY_IS_RUNNING] = false
    }
    
    private fun startTimer() {
        _uiState.value = _uiState.value.copy(isRunning = true)
        savedStateHandle[KEY_IS_RUNNING] = true

        timerJob = viewModelScope.launch {
            try {
                val startTime = System.currentTimeMillis()
                val initialTimeRemaining = _uiState.value.timeRemaining

                while (_uiState.value.isRunning && _uiState.value.timeRemaining > 0) {
                    delay(100) // Update more frequently for smoother UI

                    val elapsedSeconds = ((System.currentTimeMillis() - startTime) / 1000).toInt()
                    val currentState = _uiState.value
                    val newTime = (initialTimeRemaining - elapsedSeconds).coerceAtLeast(0)
                    val newHeatZone = calculateHeatZone(newTime, currentState.timerMode)

                    // Check for heat zone transition for haptic feedback
                    val heatZoneChanged = newHeatZone != currentState.currentHeatZone

                    _uiState.value = currentState.copy(
                        timeRemaining = newTime,
                        currentHeatZone = newHeatZone,
                        heatZoneChanged = heatZoneChanged
                    )

                    // Persist state
                    savedStateHandle[KEY_TIME_REMAINING] = newTime

                    // Reset heat zone change flag after a brief moment
                    if (heatZoneChanged) {
                        delay(100)
                        _uiState.value = _uiState.value.copy(heatZoneChanged = false)
                    }
                }

                // Timer completed
                if (_uiState.value.timeRemaining <= 0) {
                    _uiState.value = _uiState.value.copy(isRunning = false)
                    savedStateHandle[KEY_IS_RUNNING] = false
                    // TODO: Trigger notification/vibration for completion
                }
            } catch (e: CancellationException) {
                // Timer was cancelled - this is expected behavior
                throw e
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isRunning = false,
                    errorMessage = "Timer error: ${e.message}"
                )
                savedStateHandle[KEY_IS_RUNNING] = false
            }
        }
    }
    
    private fun pauseTimer() {
        _uiState.value = _uiState.value.copy(isRunning = false)
        savedStateHandle[KEY_IS_RUNNING] = false
        timerJob?.cancel()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
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