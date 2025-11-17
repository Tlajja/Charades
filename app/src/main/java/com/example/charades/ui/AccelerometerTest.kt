package com.example.charades.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.charades.sensors.rememberAccelerometer

@Composable
fun AccelerometerTest() {
    val tilt = rememberAccelerometer()
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1a1a1a)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .padding(32.dp)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = "Accelerometer Test",
                fontSize = 28.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            // X Value
            Text(
                text = "X (Left/Right)",
                fontSize = 16.sp,
                color = Color.Gray
            )
            Text(
                text = String.format("%.2f", tilt.x),
                fontSize = 48.sp,
                color = Color(0xFFFF6B6B),
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Y Value (Important one!)
            Text(
                text = "Y (Forward/Back)",
                fontSize = 16.sp,
                color = Color.Gray
            )
            Text(
                text = String.format("%.2f", tilt.y),
                fontSize = 48.sp,
                color = Color(0xFF4ECDC4),
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Z Value
            Text(
                text = "Z (Up/Down)",
                fontSize = 16.sp,
                color = Color.Gray
            )
            Text(
                text = String.format("%.2f", tilt.z),
                fontSize = 48.sp,
                color = Color(0xFFFFE66D),
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Status Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(
                        when {
                            tilt.y > 7.0f -> Color(0xFF4CAF50)   // Green
                            tilt.y < -7.0f -> Color(0xFFF44336)  // Red
                            else -> Color(0xFF757575)             // Gray
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when {
                        tilt.y > 7.0f -> "✓ CORRECT"
                        tilt.y < -7.0f -> "✗ SKIP"
                        else -> "Hold Upright"
                    },
                    fontSize = 32.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "Tilt phone forward/backward",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}