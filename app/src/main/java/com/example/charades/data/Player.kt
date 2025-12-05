package com.example.charades.data

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Player(
    val id: String,
    val name: String,
    val score: Int = 0
)

data class GameMode(
    val isSinglePlayer: Boolean = true,
    val players: List<Player> = emptyList(),
    val currentPlayerIndex: Int = 0
) {
    fun getCurrentPlayer(): Player? {
        return players.getOrNull(currentPlayerIndex)
    }

    fun getNextPlayerIndex(): Int {
        return (currentPlayerIndex + 1) % players.size
    }

    fun updatePlayerScore(playerId: String, points: Int): GameMode {
        val updatedPlayers = players.map { player ->
            if (player.id == playerId) player.copy(score = player.score + points) else player
        }
        return copy(players = updatedPlayers)
    }

    fun getSortedPlayers(): List<Player> {
        return players.sortedByDescending { it.score }
    }
}