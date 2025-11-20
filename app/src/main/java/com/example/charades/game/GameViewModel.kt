package com.example.charades.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.charades.data.WordRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class GameState(
    val points: Int = 0,
    val currentWord: String = "",
    val usedWords: Set<String> = emptySet(),
    val timeLeft: Int = 60,
    val isGameActive: Boolean = false,
    val isCountdownVisible: Boolean = false,
    val countdownValue: Int = 3
)

class GameViewModel(private val repository: WordRepository) : ViewModel() {
    var gameState by mutableStateOf(GameState())
        private set

    private var timerJob: Job? = null

    fun startGame(onTimeUp: () -> Unit) {
        if (gameState.isGameActive) return // Don't restart if already active

        gameState = gameState.copy(isGameActive = true)

        viewModelScope.launch {
            gameState = gameState.copy(isCountdownVisible = true, countdownValue = 3)
            delay(1000)
            gameState = gameState.copy(countdownValue = 2)
            delay(1000)
            gameState = gameState.copy(countdownValue = 1)
            delay(1000)

            gameState = gameState.copy(isCountdownVisible = false)
            getNextWord()
            startMainTimer(onTimeUp)
        }
    }

    private fun startMainTimer(onTimeUp: () -> Unit) {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (gameState.timeLeft > 0) {
                delay(1000)
                gameState = gameState.copy(timeLeft = gameState.timeLeft - 1)
            }
            onTimeUp()
        }
    }

    fun getNextWord() {
        val allWords = repository.loadAllWords()
        val availableWords = allWords.filterNot { it in gameState.usedWords }

        val nextWord = if (availableWords.isNotEmpty()) {
            availableWords.random()
        } else {
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
        timerJob?.cancel()
        gameState = GameState()
    }
}
