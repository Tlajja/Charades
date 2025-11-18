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
    val timeLeft: Int = 60
)

class GameViewModel(private val repository: WordRepository) : ViewModel() {
    var gameState by mutableStateOf(GameState())
        private set

    private var timerJob: Job? = null

    fun startTimer(onTimeUp: () -> Unit) {
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
