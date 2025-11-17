package com.example.charades.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.charades.data.WordRepository

data class GameState(
    val points: Int = 0,
    val currentWord: String = "",
    val usedWords: Set<String> = emptySet()
)

class GameViewModel(private val repository: WordRepository) : ViewModel() {
    var gameState by mutableStateOf(GameState())

    fun getNextWord() {
        val allWords = repository.loadAllWords()
        val availableWords = allWords.filterNot { it in gameState.usedWords }

        val nextWord = if (availableWords.isNotEmpty()) {
            availableWords.random()
        } else {
            // All words used, reset
            gameState = gameState.copy(usedWords = emptySet())
            allWords.random()
        }

        gameState = gameState.copy(
            currentWord = nextWord,
            usedWords = gameState.usedWords + nextWord
        )
    }

    fun markCorrect() {
        gameState = gameState.copy(points = gameState.points + 1)
    }

    fun resetGame() {
        gameState = GameState()
    }

}
