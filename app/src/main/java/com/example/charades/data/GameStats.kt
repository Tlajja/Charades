package com.example.charades.data

import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Serializable
data class GameResult(
    val id: String = System.currentTimeMillis().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val category: String?, // null means "All categories"
    val timerSeconds: Int, // 0 means unlimited
    val players: List<Player> = emptyList(),
    val score: Int = 0
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

    val isMultiplayer: Boolean
        get() = players.isNotEmpty()

    val totalScore: Int
        get() = if (isMultiplayer) players.sumOf { it.score } else score
}

@Serializable
data class GameStatistics(
    val games: List<GameResult> = emptyList(),
    val seenWords: Set<String> = emptySet()
) {
    fun getTotalGames(): Int = games.size

    fun getAverageScore(): Double {
        if (games.isEmpty()) return 0.0
        return games.map { it.totalScore }.average()
    }

    fun getHighScore(): Int = games.maxOfOrNull { it.totalScore } ?: 0

    fun getTotalPoints(): Int = games.sumOf { it.totalScore }

    fun getRecentGames(limit: Int = 10): List<GameResult> {
        return games.sortedByDescending { it.timestamp }.take(limit)
    }
}