package com.example.charades.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.charades.data.Category
import com.example.charades.data.CustomCategoryRepository
import com.example.charades.data.GameMode
import com.example.charades.data.GameResult
import com.example.charades.data.Player
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
    private val statsRepository: StatsRepository,
    private val customCategoryRepository: CustomCategoryRepository
) : ViewModel() {

    private val _gameEnded = MutableStateFlow(false)
    val gameEnded = _gameEnded.asStateFlow()

    fun consumeGameEndEvent() {
        _gameEnded.value = false
    }

    private val _multiplayerGameEnded = MutableStateFlow(false)
    val multiplayerGameEnded = _multiplayerGameEnded.asStateFlow()

    fun consumeMultiplayerGameEndEvent() {
        _multiplayerGameEnded.value = false
    }

    private val _goToNextPlayer = MutableStateFlow(false)
    val goToNextPlayer = _goToNextPlayer.asStateFlow()

    fun consumeNextPlayerEvent() { _goToNextPlayer.value = false }


    fun setVibrationEnabled(enabled: Boolean) {
        gameState = gameState.copy(vibrationEnabled = enabled)
    }

    fun setSoundEnabled(enabled: Boolean) {
        gameState = gameState.copy(soundEnabled = enabled)
    }

    var gameState by mutableStateOf(GameState())
        private set

    var gameMode by mutableStateOf(GameMode())
        private set

    var timerSetting by mutableStateOf(60)
        private set

    var selectedCategory by mutableStateOf<Category?>(null)
        private set

    var customCategories by mutableStateOf<List<Category.Custom>>(emptyList())
        private set

    var dontRepeatWords by mutableStateOf(false)
        private set

    private var timerJob: Job? = null

    init {
        customCategories = customCategoryRepository.loadCategories()
    }


    fun addCustomCategory(category: Category.Custom) {
        customCategories = customCategories + category
        customCategoryRepository.saveCategories(customCategories)
    }

    fun updateCustomCategory(old: Category.Custom, updated: Category.Custom) {
        customCategories = customCategories.map { if (it == old) updated else it }
        if (selectedCategory == old) selectedCategory = updated
        customCategoryRepository.saveCategories(customCategories)
    }

    fun deleteCustomCategory(category: Category.Custom) {
        customCategories = customCategories.filterNot { it == category }
        if (selectedCategory == category) selectedCategory = null
        customCategoryRepository.saveCategories(customCategories)
    }


    fun setTimer(seconds: Int) {
        timerSetting = seconds
    }

    fun setCategory(category: Category?) {
        selectedCategory = category
    }

    fun onDontRepeatWordsChanged(enabled: Boolean) {
        dontRepeatWords = enabled
        if (!enabled) {
            statsRepository.clearSeenWords()
        }
    }

    fun prepareNewGame() {
        timerJob?.cancel()
        if(gameMode.isSinglePlayer) {
            gameState = GameState(
                timeLeft = if (timerSetting == 0) Int.MAX_VALUE else timerSetting,
                vibrationEnabled = gameState.vibrationEnabled,
                soundEnabled = gameState.soundEnabled
            )
        } else {
            gameState = GameState(
                timeLeft = if (timerSetting == 0) Int.MAX_VALUE else timerSetting,
                points = 0,
                vibrationEnabled = gameState.vibrationEnabled,
                soundEnabled = gameState.soundEnabled
            )
        }
    }

    fun moveToNextPlayer() {
        val currentPlayer = gameMode.getCurrentPlayer()
        currentPlayer?.let {
            gameMode = gameMode.updatePlayerScore(it.id, gameState.points)
        }

        gameMode = gameMode.copy(currentPlayerIndex = gameMode.getNextPlayerIndex())
    }

    fun setMultiplayerMode(playerNames: List<String>) {
        val players = playerNames.mapIndexed { index, name -> Player(id = index.toString(), name = name) }
        gameMode = GameMode (
            isSinglePlayer = false,
            players = players,
            currentPlayerIndex = 0
        )
    }

    fun setGameModeToSinglePlayer() {
        gameMode = GameMode()
    }

    fun resetMultiplayerGame() {
        val playersWithResetScores = gameMode.players.map { it.copy(score = 0) }
        gameMode = gameMode.copy(players = playersWithResetScores, currentPlayerIndex = 0)
        prepareNewGame()
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
            endTurn()
        }
    }

    fun onTurnEnd() {
        if (gameState.timeLeft <= 0) {
            endTurn()
        } else {
            getNextWord()
        }
    }

    private fun endTurn() {
        if (gameMode.isSinglePlayer) {
            saveGameResult()
            _gameEnded.value = true
        } else {
            // Save current player score
            val currentPlayer = gameMode.getCurrentPlayer()
            currentPlayer?.let {
                gameMode = gameMode.updatePlayerScore(it.id, gameState.points)
            }

            // Check if last player
            val nextIndex = gameMode.getNextPlayerIndex()
            if (nextIndex == 0) {
                // all players played
                saveGameResult()
                _multiplayerGameEnded.value = true
            } else {
                // go to next player
                gameMode = gameMode.copy(currentPlayerIndex = nextIndex)
                _goToNextPlayer.value = true   // new flow event
            }
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

        val seenWords = if (dontRepeatWords) statsRepository.loadStatistics().seenWords else emptySet()
        val availableWords = allWords.filterNot { it in gameState.usedWords || it in seenWords }

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
        val result = if (gameMode.isSinglePlayer) {
            GameResult(
                score = gameState.points,
                category = selectedCategory?.displayName,
                timerSeconds = timerSetting
            )
        } else {
            GameResult(
                category = selectedCategory?.displayName,
                timerSeconds = timerSetting,
                players = gameMode.players
            )
        }
        statsRepository.saveGameResult(result, gameState.usedWords)
    }

    fun resetGame() {
        timerJob?.cancel()
        gameState = GameState(
            vibrationEnabled = gameState.vibrationEnabled,
            soundEnabled = gameState.soundEnabled
        )
    }
}