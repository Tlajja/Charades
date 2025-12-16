package com.tlajja.charades.data

import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val id: String,
    val name: String,
    val score: Int = 0
)

data class GameMode(
    val isSinglePlayer: Boolean = true,
    val isTeamMode: Boolean = false,
    val players: List<Player> = emptyList(),
    val currentPlayerIndex: Int = 0,
    val totalRounds: Int = 1,
    val currentRound: Int = 1
) {
    fun getCurrentPlayer(): Player? = players.getOrNull(currentPlayerIndex)
    fun getNextPlayerIndex(): Int = (currentPlayerIndex + 1) % players.size

    fun updatePlayerScore(playerId: String, points: Int): GameMode {
        val updated = players.map { p -> if (p.id == playerId) p.copy(score = p.score + points) else p }
        return copy(players = updated)
    }

    fun getSortedPlayers(): List<Player> = players.sortedByDescending { it.score }
}
