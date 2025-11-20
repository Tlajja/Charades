package com.example.charades.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.charades.sensors.rememberGyroscope

@Composable
fun GyroscopeTestView() {
    val gyroscope = rememberGyroscope()
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2d2d2d)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = "Gyroscope Test",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(32.dp))

            // X Value
            Text(
                text = "X Axis (Pitch)",
                fontSize = 18.sp,
                color = Color.Gray
            )
            Text(
                text = String.format("%.2f", gyroscope.x),
                fontSize = 48.sp,
                color = Color(0xFF82B1FF),
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Y Value
            Text(
                text = "Y Axis (Roll)",
                fontSize = 18.sp,
                color = Color.Gray
            )
            Text(
                text = String.format("%.2f", gyroscope.y),
                fontSize = 48.sp,
                color = Color(0xFFC3E88D),
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Z Value
            Text(
                text = "Z Axis (Yaw)",
                fontSize = 18.sp,
                color = Color.Gray
            )
            Text(
                text = String.format("%.2f", gyroscope.z),
                fontSize = 48.sp,
                color = Color(0xFFFFB74D),
                fontWeight = FontWeight.Bold
            )
        }
    }
}
