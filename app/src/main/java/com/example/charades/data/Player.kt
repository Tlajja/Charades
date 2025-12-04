package com.example.charades.data

import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val id: String = System.currentTimeMillis().toString(),
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

    fun updatePlayerScore(playerId: String, newScore: Int): GameMode {
        val updatedPlayers = players.map { player ->
            if (player.id == playerId) player.copy(score = newScore) else player
        }
        return copy(players = updatedPlayers)
    }

    fun getSortedPlayers(): List<Player> {
        return players.sortedByDescending { it.score }
    }
}