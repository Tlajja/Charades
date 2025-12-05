package com.example.charades.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import com.example.charades.R
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.charades.data.Player

@Composable
fun MultiplayerResultsView(players: List<Player>, onBackToMenu: () -> Unit) {
    Box(Modifier.fillMaxSize(), Alignment.Center) {

        Image(
            painter = painterResource(id = R.drawable.bluebg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                "Rezultatai",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = Color.White
            )

            players.sortedByDescending { it.score }.forEachIndexed { index, player ->
                Text(
                    "${index + 1}. ${player.name} — ${player.score} taškų",
                    fontSize = 24.sp,
                    color = Color.White,
                    fontFamily = FontFamily.Monospace
                )
            }

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = onBackToMenu,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50),
                    contentColor = Color.White
                )
            ) {
                Text("Grįžti į pradžią")
            }
        }
    }
}

@Preview
@Composable
fun MultiplayerResultsPreview(){
    MultiplayerResultsView(
        players = listOf(
            Player(1.toString(), "Jonas", 10),
            Player(2.toString(), "Petras", 20),
            Player(3.toString(), "Antanas", 30)
        ),
        onBackToMenu = {}
    )
}

