package com.example.charades.ui

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.charades.R
import com.example.charades.audio.SoundManager

@Composable
fun GameResultsView(
    points: Int,
    onPlayAgain: () -> Unit,
    onGoToStart: () -> Unit,
    category: String?,
    timerSettings: Int,
    vibrationEnabled: Boolean,
    soundEnabled: Boolean,
    soundManager: SoundManager
) {
    val context = LocalContext.current
    val vibrator = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vm = context.getSystemService(VibratorManager::class.java)
            vm.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Vibrator::class.java)
        }
    }

    var gameEndEffectTriggered by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!gameEndEffectTriggered) {
            if (vibrationEnabled) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    // A three-buzz pattern to signify the end of the game
                    val timings = longArrayOf(0, 200, 100, 200, 100, 200)
                    val amplitudes = intArrayOf(0, VibrationEffect.DEFAULT_AMPLITUDE, 0, VibrationEffect.DEFAULT_AMPLITUDE, 0, VibrationEffect.DEFAULT_AMPLITUDE)
                    val effect = VibrationEffect.createWaveform(timings, amplitudes, -1)
                    vibrator?.vibrate(effect)
                } else {
                    // Fallback for older APIs: A single, longer vibration
                    @Suppress("DEPRECATION")
                    vibrator?.vibrate(600) // Increased duration to feel more final
                }
            }
            if (soundEnabled) {
                soundManager.playGameEnd()
            }
            gameEndEffectTriggered = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.bluebg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.4f),
                            Color.Black.copy(alpha = 0.8f)
                        )
                    )
                )
        )

        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .widthIn(max = 520.dp)
                .background(
                    color = Color.Black.copy(alpha = 0.55f),
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(vertical = 20.dp, horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Laikas baigėsi!",
                    fontSize = 26.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )

                Text(
                    text = "Surinkti taškai",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.85f),
                    fontFamily = FontFamily.Monospace
                )

                Text(
                    text = points.toString(),
                    fontSize = 52.sp,
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )

                Text(
                    text = "Kategorija: ${category ?: "Visi"}",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.85f),
                    fontFamily = FontFamily.Monospace
                )

                if (timerSettings > 0) {
                    Text(
                        text = "Laikmatis: $timerSettings s",
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.85f),
                        fontFamily = FontFamily.Monospace
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = onPlayAgain,
                        modifier = Modifier
                            .weight(1f)
                            .height(55.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 6.dp,
                            pressedElevation = 10.dp
                        )
                    ) {
                        Text(
                            text = "Žaisti dar kartą",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Button(
                        onClick = onGoToStart,
                        modifier = Modifier
                            .weight(1f)
                            .height(55.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0B41BB),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 6.dp,
                            pressedElevation = 10.dp
                        )
                    ) {
                        Text(
                            text = "Grįžti į pradžią",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun GameResultsViewPreview() {
    GameResultsView(
        points = 12,
        onPlayAgain = {},
        onGoToStart = {},
        category = "Gyvūnai",
        timerSettings = 60,
        vibrationEnabled = true,
        soundEnabled = true,
        soundManager = SoundManager(LocalContext.current)
    )
}
