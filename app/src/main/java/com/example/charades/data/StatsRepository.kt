package com.example.charades.data

import android.content.Context
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class StatsRepository(private val context: Context) {
    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }
    private val statsFile = File(context.filesDir, "game_statistics.json")

    fun saveGameResult(result: GameResult, usedWords: Set<String>) {
        val currentStats = loadStatistics()
        val updatedGames = currentStats.games + result
        val updatedSeenWords = currentStats.seenWords + usedWords
        val updatedStats = GameStatistics(updatedGames, updatedSeenWords)

        try {
            val jsonString = json.encodeToString(updatedStats)
            statsFile.writeText(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadStatistics(): GameStatistics {
        return try {
            if (statsFile.exists()) {
                val jsonString = statsFile.readText()
                json.decodeFromString<GameStatistics>(jsonString)
            } else {
                GameStatistics()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            GameStatistics()
        }
    }
    
    fun clearSeenWords() {
        val currentStats = loadStatistics()
        val updatedStats = currentStats.copy(seenWords = emptySet())
        try {
            val jsonString = json.encodeToString(updatedStats)
            statsFile.writeText(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun clearStatistics() {
        try {
            if (statsFile.exists()) {
                statsFile.delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}