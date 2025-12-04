package com.example.charades.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.charades.data.Category

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GameSettingsView(
    timerValue: Int,
    selectedCategory: Category?,
    onTimerChange: (Int) -> Unit,
    onCategoryChange: (Category?) -> Unit,
    onStartClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Box(
        contentAlignment = Alignment.Center,
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
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(32.dp, 64.dp, 32.dp, 0.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp)) // Space for the back button
            Text(
                text = "NUSTATYMAI",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(48.dp))

            // Timer setting
            Text(
                text = "LAIKMATIS (sekundės)",
                fontSize = 18.sp,
                fontFamily = FontFamily.Monospace,
                color = Color.White.copy(alpha = 0.8f)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = {
                        if (timerValue > 15) {
                            onTimerChange(timerValue - 15)
                        } else if (timerValue == 15) {
                            onTimerChange(0) // Set to unlimited
                        }
                    },
                    enabled = timerValue > 0
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Sumažinti laiką",
                        tint = if (timerValue > 0) Color.White else Color.Gray
                    )
                }
                Text(
                    text = if (timerValue == 0) "∞" else timerValue.toString(),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                IconButton(onClick = {
                    if (timerValue == 0) {
                        onTimerChange(15) // From unlimited to 15
                    } else {
                        onTimerChange(timerValue + 15)
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Padidinti laiką",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Category setting
            Text(
                text = "KATEGORIJOS",
                fontSize = 18.sp,
                fontFamily = FontFamily.Monospace,
                color = Color.White.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CategoryButton(
                    text = "Visi",
                    isSelected = selectedCategory == null,
                    onClick = { onCategoryChange(null) }
                )

                Category.entries.forEach {
                    CategoryButton(
                        text = it.displayName,
                        isSelected = selectedCategory == it,
                        onClick = { onCategoryChange(it) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Start Button
            Button(
                onClick = onStartClick,
                modifier = Modifier
                    .width(200.dp)
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
            ) {
                Text(
                    text = "PRADĖTI",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
        
        // Back Button
        IconButton(
            onClick = onBackClick,
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
    }
}

@Composable
fun CategoryButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val colors = if (isSelected) {
        ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50), contentColor = Color.White)
    } else {
        ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
    }

    Button(
        onClick = onClick,
        colors = colors,
    ) {
        Text(text)
    }
}


@Preview
@Composable
fun GameSettingsPreview(){
    GameSettingsView(60, null, {}, {}, {}, {})
}
