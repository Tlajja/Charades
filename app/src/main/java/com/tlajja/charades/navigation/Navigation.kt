package com.tlajja.charades.navigation

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tlajja.charades.audio.rememberSoundManager
import com.tlajja.charades.data.CustomCategoryRepository
import com.tlajja.charades.data.StatsRepository
import com.tlajja.charades.data.WordRepository
import com.tlajja.charades.game.GameViewModel
import com.tlajja.charades.ui.*
import kotlinx.coroutines.delay
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


@Composable
fun CharadesNavigation(inAppForeground: Boolean) {
    val navController = rememberNavController()
    val context = LocalContext.current

    val wordRepository = remember { WordRepository(context) }
    val statsRepository = remember { StatsRepository(context) }
    val customCategoryRepository = remember { CustomCategoryRepository(context) }

    val viewModel: GameViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return GameViewModel(
                    wordRepository,
                    statsRepository,
                    customCategoryRepository
                ) as T
            }
        }
    )

    val soundManager = rememberSoundManager()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    ManageSystemUi(route = navBackStackEntry?.destination?.route)

    val gameHasEnded by viewModel.gameEnded.collectAsState()
    val multiplayerGameHasEnded by viewModel.multiplayerGameEnded.collectAsState()

    LaunchedEffect(gameHasEnded) {
        if (gameHasEnded) {
            navController.navigate("game_over") {
                popUpTo("start") { inclusive = false }
            }
            viewModel.consumeGameEndEvent()
        }
    }

    LaunchedEffect(multiplayerGameHasEnded) {
        if (multiplayerGameHasEnded) {
            navController.navigate("multiplayer_results") {
                popUpTo("start") { inclusive = false }
            }
            viewModel.consumeMultiplayerGameEndEvent()
        }
    }

    val nextPlayerEvent by viewModel.goToNextPlayer.collectAsState()

    LaunchedEffect(nextPlayerEvent) {
        if (nextPlayerEvent) {
            navController.navigate("player_transition") {
                popUpTo("start") { inclusive = false }
            }
            viewModel.consumeNextPlayerEvent()
        }
    }

    NavHost(
        navController = navController,
        startDestination = "start"
    ) {
        composable("start") {
            GameStartScreen(
                onStartClick = { navController.navigate("settings") },
                onStatisticsClick = { navController.navigate("statistics") }
            )
        }

        composable("statistics") {
            val stats = statsRepository.loadStatistics()
            StatisticsView(
                statistics = stats,
                onBackClick = { navController.navigateUp() },
                onClearStats = {
                    statsRepository.clearStatistics()
                    navController.navigateUp()
                }
            )
        }


        composable("settings") {
            GameSettingsView(
                timerValue = viewModel.timerSetting,
                selectedCategory = viewModel.selectedCategory,
                customCategories = viewModel.customCategories,
                onTimerChange = { viewModel.setTimer(it) },
                onCategoryChange = { viewModel.setCategory(it) },
                onManageCustomCategoriesClick = { navController.navigate("custom_categories") },
                soundEnabled = viewModel.gameState.soundEnabled,
                vibrationEnabled = viewModel.gameState.vibrationEnabled,
                onVibrationChange = { viewModel.setVibrationEnabled(it) },
                onSoundChange = { viewModel.setSoundEnabled(it) },
                dontRepeatWords = viewModel.dontRepeatWords,
                onDontRepeatWordsChange = { viewModel.onDontRepeatWordsChanged(it) },
                onStartClick = {
                    viewModel.setGameModeToSinglePlayer()
                    viewModel.prepareNewGame()
                    navController.navigate("word")
                },
                onBackClick = { navController.navigateUp() },
                onSetPlayersClick = { navController.navigate("set_players") }
            )
        }


        composable("custom_categories") {
            CustomCategoryView(
                categories = viewModel.customCategories,
                onSaveNew = { viewModel.addCustomCategory(it) },
                onUpdateCategory = { old, updated -> viewModel.updateCustomCategory(old, updated) },
                onDeleteCategory = { toDelete -> viewModel.deleteCustomCategory(toDelete) },
                onBackClick = { navController.navigateUp() }
            )
        }

        composable("set_players") {
            SetPlayersView(
                onBackClick = { navController.navigateUp() },
                onStartClick = { playerNames ->
                    viewModel.setMultiplayerMode(playerNames)
                    navController.navigate("player_transition")
                }
            )
        }

        composable("word") {
            LaunchedEffect(Unit) {
                viewModel.startGame()
            }

            WordView(
                word = viewModel.gameState.currentWord,
                timeLeft = viewModel.gameState.timeLeft,
                isCountdownVisible = viewModel.gameState.isCountdownVisible,
                countdownValue = viewModel.gameState.countdownValue,
                vibrationEnabled = viewModel.gameState.vibrationEnabled,
                soundEnabled = viewModel.gameState.soundEnabled,
                soundManager = soundManager,
                onCorrect = {
                    viewModel.markCorrect()
                    navController.navigate("correct")
                },
                onSkip = {
                    navController.navigate("skip")
                },
                onBack = {
                    navController.popBackStack("start", inclusive = false)
                },
                onPauseTimer = { viewModel.pauseTimer() },
                onResumeTimer = { viewModel.resumeTimer() },
                inAppForeground = inAppForeground
            )
        }

        composable("correct") {
            CorrectView()
            LaunchedEffect(Unit) {
                delay(1000)
                viewModel.onTurnEnd()
                if (viewModel.gameState.timeLeft > 0) {
                    navController.navigate("word") {
                        popUpTo("word") { inclusive = true }
                    }
                }
            }
        }

        composable("skip") {
            SkipView()
            LaunchedEffect(Unit) {
                delay(1000)
                viewModel.onTurnEnd()
                if (viewModel.gameState.timeLeft > 0) {
                    navController.navigate("word") {
                        popUpTo("word") { inclusive = true }
                    }
                }
            }
        }

        composable("game_over") {
            GameResultsView(
                points = viewModel.gameState.points,
                onPlayAgain = {
                    viewModel.setGameModeToSinglePlayer()
                    viewModel.prepareNewGame()
                    navController.navigate("word") {
                        popUpTo("start") { inclusive = false }
                    }
                },
                onGoToStart = {
                    navController.popBackStack("start", inclusive = false)
                },
                category = viewModel.selectedCategory?.displayName,
                timerSettings = viewModel.timerSetting,
                vibrationEnabled = viewModel.gameState.vibrationEnabled,
                soundEnabled = viewModel.gameState.soundEnabled,
                soundManager = soundManager
            )
        }

        composable("player_transition") {
            val currentPlayer = viewModel.gameMode.getCurrentPlayer()
            PlayerTransitionView(
                playerName = currentPlayer?.name ?: "",
                currentScore = currentPlayer?.score ?: 0,
                onStartTurn = {
                    viewModel.prepareNewGame()
                    navController.navigate("word")
                }
            )
        }

        composable("multiplayer_results") {
            MultiplayerResultsView(
                players = viewModel.gameMode.getSortedPlayers(),
                onBackToMenu = {
                    navController.popBackStack("start", inclusive = false)
                },
                onPlayAgain = {
                    viewModel.resetMultiplayerGame()
                    navController.navigate("player_transition")
                },
                category = viewModel.selectedCategory?.displayName,
                timerSettings = viewModel.timerSetting,
                vibrationEnabled = viewModel.gameState.vibrationEnabled,
                soundEnabled = viewModel.gameState.soundEnabled,
                soundManager = soundManager
            )
        }
    }
}

@Composable
private fun ManageSystemUi(route: String?) {
    val isLandscapeOnly = route in listOf("word", "correct", "skip")
    val context = LocalContext.current

    // Effect to hide system bars.
    // It runs on every route change to ensure the bars stay hidden.
    DisposableEffect(route) {
        val activity = context as? Activity ?: return@DisposableEffect onDispose {}
        val window = activity.window
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)

        insetsController.hide(WindowInsetsCompat.Type.systemBars())
        insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        onDispose {}
    }

    // Effect to manage screen orientation.
    // It only runs when the landscape requirement changes to prevent flickering between landscape screens.
    DisposableEffect(isLandscapeOnly) {
        val activity = context as? Activity ?: return@DisposableEffect onDispose {}

        if (isLandscapeOnly) {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }

        onDispose {
            // When leaving a landscape screen, unlock the orientation.
            if (isLandscapeOnly) {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            }
        }
    }
}
