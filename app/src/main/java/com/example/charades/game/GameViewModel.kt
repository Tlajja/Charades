package com.example.charades.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.charades.data.Category
import com.example.charades.data.GameResult
import com.example.charades.data.StatsRepository
import com.example.charades.data.WordRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class GameState(
    val points: Int = 0,
    val currentWord: String = "",
    val usedWords: Set<String> = emptySet(),
    val timeLeft: Int = 60,
    val isGameActive: Boolean = false,
    val isCountdownVisible: Boolean = false,
    val countdownValue: Int = 3,
    val vibrationEnabled: Boolean = true,
    val soundEnabled: Boolean = true
)

class GameViewModel(
    private val repository: WordRepository,
    private val statsRepository: StatsRepository
) : ViewModel() {

    private val _gameEnded = MutableStateFlow(false)
    val gameEnded = _gameEnded.asStateFlow()

    fun consumeGameEndEvent() {
        _gameEnded.value = false
    }

    fun setVibrationEnabled(enabled: Boolean) {
        gameState = gameState.copy(vibrationEnabled = enabled)
    }

    fun setSoundEnabled(enabled: Boolean) {
        gameState = gameState.copy(soundEnabled = enabled)
    }

    var gameState by mutableStateOf(GameState())
        private set

    var timerSetting by mutableStateOf(60)
        private set

    var selectedCategory by mutableStateOf<Category?>(null)
        private set

    private var timerJob: Job? = null

    fun setTimer(seconds: Int) {
        timerSetting = seconds
    }

    fun setCategory(category: Category?) {
        selectedCategory = category
    }

    fun prepareNewGame() {
        timerJob?.cancel()
        gameState = GameState(
            timeLeft = if (timerSetting == 0) Int.MAX_VALUE else timerSetting,
            vibrationEnabled = gameState.vibrationEnabled,
            soundEnabled = gameState.soundEnabled
        )
    }

    fun startGame() {
        if (gameState.isGameActive) return

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
            if (timerSetting > 0) {
                startMainTimer()
            }
        }
    }

    private fun startMainTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            var time = gameState.timeLeft
            while (time > 0) {
                delay(1000)
                time--
                gameState = gameState.copy(timeLeft = time)
            }
            saveGameResult()
            _gameEnded.value = true
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    fun resumeTimer() {
        if (timerJob != null) return
        if (!gameState.isGameActive) return
        if (gameState.isCountdownVisible) return
        if (timerSetting == 0) return
        if (gameState.timeLeft <= 0) return

        startMainTimer()
    }

    fun getNextWord() {
        val allWords = if (selectedCategory != null) {
            repository.loadWordsFromCategory(selectedCategory!!)
        } else {
            repository.loadAllWords()
        }

        val availableWords = allWords.filterNot { it in gameState.usedWords }

        val nextWord = if (availableWords.isNotEmpty()) {
            availableWords.random()
        } else {
            gameState = gameState.copy(usedWords = emptySet())
            allWords.randomOrNull() ?: "ERROR"
        }

        gameState = gameState.copy(
            currentWord = nextWord,
            usedWords = gameState.usedWords + nextWord
        )
    }

    fun markCorrect() {
        gameState = gameState.copy(points = gameState.points + 1)
    }

    fun saveGameResult() {
        val result = GameResult(
            points = gameState.points,
            category = selectedCategory?.displayName,
            timerSeconds = timerSetting
        )
        statsRepository.saveGameResult(result)
    }

    fun resetGame() {
        timerJob?.cancel()
        gameState = GameState(
            vibrationEnabled = gameState.vibrationEnabled,
            soundEnabled = gameState.soundEnabled
        )
    }
}
