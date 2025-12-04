package com.example.charades.ui

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.charades.R
import com.example.charades.sensors.rememberGyroscope
import java.util.Locale

@Composable
fun WordView(
    word: String,
    timeLeft: Int,
    isCountdownVisible: Boolean,
    countdownValue: Int,
    vibrationEnabled: Boolean,
    onCorrect: () -> Unit,
    onSkip: () -> Unit,
    onBack: () -> Unit,
    onPauseTimer: () -> Unit,
    onResumeTimer: () -> Unit,
    inAppForeground: Boolean
)
 {
    val context = LocalContext.current

    LaunchedEffect(inAppForeground) {
        if (inAppForeground) {
            onResumeTimer()
        } else {
            onPauseTimer()
        }
    }

     val vibrator = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vm = context.getSystemService(VibratorManager::class.java)
            vm.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Vibrator::class.java)
        }
    }

    fun vibrateCorrect() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(
                VibrationEffect.createOneShot(
                    150,
                    255
                )
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(80)
        }
    }

    fun vibrateSkip() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(
                VibrationEffect.createWaveform(
                    longArrayOf(0, 90, 40, 120),
                    -1
                )
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(60)
        }
    }

     val gyroscope = rememberGyroscope(inAppForeground)
     var hasTriggered by remember(word) { mutableStateOf(false) }

     if (!isCountdownVisible) {
         LaunchedEffect(gyroscope.y) {
             if (hasTriggered) return@LaunchedEffect

             when {
                 gyroscope.y < -5f -> {
                     hasTriggered = true
                     if(vibrationEnabled) vibrateCorrect()
                     onCorrect()
                 }
                 gyroscope.y > 5f -> {
                     hasTriggered = true
                     if(vibrationEnabled) vibrateSkip()
                     onSkip()
                 }
             }
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
            modifier = Modifier.fillMaxSize()
        )

        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Grįžti",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }

        if (isCountdownVisible) {
            Text(
                text = countdownValue.toString(),
                fontSize = 120.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.animateContentSize()
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 64.dp, horizontal = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                var dynamicFontSize by remember(word) { mutableStateOf(72.sp) }
                var readyToDraw by remember(word) { mutableStateOf(false) }

                Text(
                    text = word.uppercase(Locale.getDefault()),
                    fontSize = dynamicFontSize,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.Center,
                    lineHeight = dynamicFontSize * 1.1f,
                    modifier = Modifier.alpha(if (readyToDraw) 1f else 0f),
                    onTextLayout = { textLayoutResult ->
                        if (textLayoutResult.didOverflowHeight) {
                            dynamicFontSize *= 0.95f
                        } else {
                            readyToDraw = true
                        }
                    }
                )
            }
        }

        if (!isCountdownVisible) {
            Text(
                text = if (timeLeft == 2147483647) "∞" else timeLeft.toString(),
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(32.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WordViewPreviewCountdown() {
    WordView(
        word = "",
        timeLeft = 60,
        isCountdownVisible = true,
        countdownValue = 3,
        vibrationEnabled = true,
        onCorrect = {},
        onSkip = {},
        onBack = {},
        onPauseTimer = {},
        onResumeTimer = {},
        inAppForeground = true
    )
}

@Preview(showBackground = true)
@Composable
fun WordViewPreviewWord() {
    WordView(
        word = "Ilgas Dvigubas Žodis Kuris Netelpa",
        timeLeft = 55,
        isCountdownVisible = false,
        countdownValue = 0,
        vibrationEnabled = true,
        onCorrect = {},
        onSkip = {},
        onBack = {},
        onPauseTimer = {},
        onResumeTimer = {},
        inAppForeground = true
    )
}
