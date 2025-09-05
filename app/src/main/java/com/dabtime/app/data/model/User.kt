package com.dabtime.app.data.model

data class User(
    val id: String = "",
    val displayName: String = "",
    val email: String = "",
    val profileImageUrl: String = "",
    val isAnonymous: Boolean = false,
    val totalDabs: Int = 0,
    val precisionScore: Double = 0.0,
    val weeklyDabs: Int = 0,
    val badges: List<String> = emptyList(),
    val friends: List<String> = emptyList(),
    val preferences: UserPreferences = UserPreferences(),
    val createdAt: Long = System.currentTimeMillis(),
    val lastActive: Long = System.currentTimeMillis()
)

data class UserPreferences(
    val defaultTimerMode: TimerMode = TimerMode.BALANCED,
    val enableHapticFeedback: Boolean = true,
    val enableSoundEffects: Boolean = true,
    val enableNotifications: Boolean = true,
    val shareSessionsByDefault: Boolean = false,
    val theme: String = "auto" // "light", "dark", "auto"
)

enum class TimerMode {
    STRICT,
    BALANCED,
    CHILL
}