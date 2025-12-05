package com.example.charades.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.charades.R
import com.example.charades.data.GameResult
import com.example.charades.data.GameStatistics

@Composable
fun StatisticsView(
    statistics: GameStatistics,
    onBackClick: () -> Unit,
    onClearStats: () -> Unit
) {
    var showClearDialog by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.bluebg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            alpha = 0.8f
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            // Header with back button
            Column (
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Grįžti",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "STATISTIKA",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Statistics Summary
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Žaista",
                    value = statistics.getTotalGames().toString(),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Rekordas",
                    value = statistics.getHighScore().toString(),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Vidurkis",
                    value = String.format("%.1f", statistics.getAverageScore()),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Recent Games Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Paskutiniai žaidimai",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White
                )

                if (statistics.getTotalGames() > 0) {
                    TextButton(onClick = { showClearDialog = true }) {
                        Text(
                            text = "Išvalyti",
                            color = Color(0xFFFF6B6B),
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Games List
            if (statistics.getTotalGames() == 0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Dar nėra sužaistų žaidimų",
                        fontSize = 18.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        fontFamily = FontFamily.Monospace
                    )
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    statistics.getRecentGames(20).forEach { game ->
                        GameResultCard(game)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            containerColor = Color(0xFF0D0D0D).copy(alpha = 0.92f),
            titleContentColor = Color.White,
            textContentColor = Color(0xFFD0D0D0),
            shape = RoundedCornerShape(24.dp),
            title = {
                Text(
                    text = "Išvalyti statistiką?",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Ar tikrai norite ištrinti visus žaidimų rezultatus? Šio veiksmo negalima atšaukti.",
                    fontFamily = FontFamily.Monospace
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onClearStats()
                        showClearDialog = false
                    }
                ) {
                    Text(
                        text = "Išvalyti",
                        color = Color(0xFFFF6B6B),
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text(
                        text = "Atšaukti",
                        color = Color.White,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        )
    }
}

@Composable
fun StatCard(title: String, value: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(
                color = Color.Black.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.7f),
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = value,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4CAF50),
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

@Composable
fun GameResultCard(game: GameResult) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.Black.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = game.getFormattedDate(),
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    fontFamily = FontFamily.Monospace
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = game.getCategoryDisplay(),
                        fontSize = 12.sp,
                        color = Color(0xFF82B1FF),
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = game.getTimerDisplay(),
                        fontSize = 12.sp,
                        color = Color(0xFFC3E88D),
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
            Text(
                text = game.points.toString(),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4CAF50),
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

@Preview
@Composable
fun StatisticsViewPreview() {
    val sampleGames = listOf(
        GameResult(points = 15, category = "Gyvūnai", timerSeconds = 60),
        GameResult(points = 12, category = null, timerSeconds = 45),
        GameResult(points = 20, category = "Sportas", timerSeconds = 0)
    )
    StatisticsView(
        statistics = GameStatistics(sampleGames),
        onBackClick = {},
        onClearStats = {}
    )
}