package com.example.charades.navigation

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import com.example.charades.data.WordRepository
import com.example.charades.game.GameViewModel
import com.example.charades.ui.CorrectView
import com.example.charades.ui.GameResultsView
import com.example.charades.ui.GameStartScreen
import com.example.charades.ui.SkipView
import com.example.charades.ui.WordView
import kotlinx.coroutines.delay

@Composable
fun CharadesNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val repository = remember { WordRepository(context) }
    val viewModel = remember { GameViewModel(repository) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    ManageSystemUi(route = navBackStackEntry?.destination?.route)

    NavHost(
        navController = navController,
        startDestination = "start"
    ) {
        composable("start") {
            GameStartScreen(
                onStartClick = {
                    viewModel.resetGame()
                    navController.navigate("word")
                }
            )
        }

        composable("word") {
            // This effect starts the game (countdown + timer)
            LaunchedEffect(Unit) {
                viewModel.startGame {
                    navController.navigate("game_over") { popUpTo("start") }
                }
            }

            WordView(
                word = viewModel.gameState.currentWord,
                timeLeft = viewModel.gameState.timeLeft,
                isCountdownVisible = viewModel.gameState.isCountdownVisible,
                countdownValue = viewModel.gameState.countdownValue,
                onCorrect = {
                    viewModel.markCorrect()
                    navController.navigate("correct")
                },
                onSkip = {
                    navController.navigate("skip")
                },
                onBack = {
                    navController.popBackStack("start", inclusive = false)
                }
            )
        }

        composable("correct") {
            CorrectView()
            LaunchedEffect(Unit) {
                delay(1000)
                viewModel.getNextWord()
                navController.navigate("word") { popUpTo("word") { inclusive = true } }
            }
        }

        composable("skip") {
            SkipView()
            LaunchedEffect(Unit) {
                delay(1000)
                viewModel.getNextWord()
                navController.navigate("word") { popUpTo("word") { inclusive = true } }
            }
        }

        composable("game_over") {
            GameResultsView(
                points = viewModel.gameState.points,
                onPlayAgain = {
                    viewModel.resetGame()
                    navController.navigate("word") { popUpTo("start") }
                },
                onGoToStart = {
                    navController.popBackStack("start", inclusive = false)
                }
            )
        }
    }
}


@Composable
private fun ManageSystemUi(route: String?) {
    val context = LocalContext.current
    val isGameScreen = route in listOf("word", "correct", "skip", "game_over")

    DisposableEffect(isGameScreen) {
        val activity = context as? Activity ?: return@DisposableEffect onDispose {}
        val window = activity.window
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)

        if (isGameScreen) {
            val originalOrientation = activity.requestedOrientation
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            insetsController.hide(WindowInsetsCompat.Type.systemBars())
            insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

            onDispose {
                activity.requestedOrientation = originalOrientation
                insetsController.show(WindowInsetsCompat.Type.systemBars())
            }
        } else {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            insetsController.show(WindowInsetsCompat.Type.systemBars())
            onDispose {}
        }
    }
}