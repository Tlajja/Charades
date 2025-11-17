package com.example.charades.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.charades.R
import com.example.charades.sensors.rememberAccelerometer

@Composable
fun WordView(
    word: String,
    onCorrect: () -> Unit,
    onSkip: () -> Unit,
    onBack: () -> Unit
) {
    val tilt = rememberAccelerometer()
    var hasTriggered by remember { mutableStateOf(false) }

    // Detect tilts and ensure it only triggers once
    LaunchedEffect(tilt.y) {
        if (!hasTriggered) {
            when {
                tilt.z > 7.0f -> {
                    onSkip()
                }
                tilt.z < -7.0f -> {
                    onCorrect()
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
            modifier = Modifier
                .fillMaxSize()
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

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            var dynamicFontSize by remember(word) { mutableStateOf(72.sp) }
            var isFontSizeCalculated by remember(word) { mutableStateOf(false) }

            Text(
                text = word.uppercase(),
                fontSize = dynamicFontSize,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = FontFamily.Monospace,
                maxLines = 1,
                softWrap = false,
                onTextLayout = {
                    if (!isFontSizeCalculated && it.hasVisualOverflow) {
                        dynamicFontSize *= 0.95f
                    } else {
                        isFontSizeCalculated = true
                    }
                }
            )
        }
    }
}

@Preview
@Composable
fun WordViewPreview() {
    WordView(
        word = "Krokodilas",
        onCorrect = {},
        onSkip = {},
        onBack = {}
    )
}
