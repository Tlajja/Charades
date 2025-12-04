package com.example.charades.data

import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Serializable
data class GameResult(
    val id: String = System.currentTimeMillis().toString(),
    val points: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val category: String?, // null means "All categories"
    val timerSeconds: Int // 0 means unlimited
) {
    fun getFormattedDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    fun getCategoryDisplay(): String {
        return category ?: "Visi"
    }

    fun getTimerDisplay(): String {
        return if (timerSeconds == 0) "∞" else "${timerSeconds}s"
    }
}

@Serializable
data class GameStatistics(
    val games: List<GameResult> = emptyList()
) {
    fun getTotalGames(): Int = games.size

    fun getAverageScore(): Double {
        if (games.isEmpty()) return 0.0
        return games.map { it.points }.average()
    }

    fun getHighScore(): Int = games.maxOfOrNull { it.points } ?: 0

    fun getTotalPoints(): Int = games.sumOf { it.points }

    fun getRecentGames(limit: Int = 10): List<GameResult> {
        return games.sortedByDescending { it.timestamp }.take(limit)
    }
}