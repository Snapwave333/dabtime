package com.dabtime.app.data.model

data class DabSession(
    val id: String = "",
    val userId: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val timerMode: TimerMode = TimerMode.BALANCED,
    val heatZone: HeatZone = HeatZone.NOT_READY,
    val duration: Long = 0L, // Duration in milliseconds
    val precisionScore: Double = 0.0, // 0.0 to 100.0
    val isSharedWithFriends: Boolean = false,
    val notes: String = "",
    val location: String = "", // Optional location tag
    val temperature: Int = 0, // Temperature in Fahrenheit (optional)
    val dabType: String = "", // Type of concentrate (optional)
    val rigType: String = "", // Type of rig used (optional)
    val sessionRating: Int = 0 // 1-5 star rating (optional)
)

enum class HeatZone {
    NOT_READY,    // Black - Dab not ready
    LOW_TEMP,     // Blue - Low temp dab
    MEDIUM_TEMP,  // Orange - Medium temp dab
    HIGH_TEMP     // Red - High temp dab
}

data class SessionStats(
    val totalSessions: Int = 0,
    val averagePrecisionScore: Double = 0.0,
    val favoriteHeatZone: HeatZone = HeatZone.MEDIUM_TEMP,
    val favoriteTimerMode: TimerMode = TimerMode.BALANCED,
    val totalDabTime: Long = 0L, // Total time spent dabbing in milliseconds
    val bestPrecisionScore: Double = 0.0,
    val currentStreak: Int = 0, // Current daily streak
    val longestStreak: Int = 0, // Longest daily streak
    val weeklyStats: WeeklyStats = WeeklyStats()
)

data class WeeklyStats(
    val weekStartTimestamp: Long = 0L,
    val sessionsThisWeek: Int = 0,
    val averageScoreThisWeek: Double = 0.0,
    val rank: Int = 0 // Weekly leaderboard rank
)