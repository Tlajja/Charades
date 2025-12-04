package com.example.charades.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

@Composable
fun GameStartScreen(
    onStartClick: () -> Unit
) {
    var showRulesDialog by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
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
                            Color.Black.copy(alpha = 0.3f),
                            Color.Black.copy(alpha = 0.6f)
                        )
                    )
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .widthIn(max = 520.dp)
                .align(Alignment.Center)
        ) {
            Text(
                text = "ŠARADAI",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = Color.White,
                letterSpacing = 4.sp
            )
            Text(
                text = "Rodyk, spėk, laimėk!",
                fontSize = 18.sp,
                fontFamily = FontFamily.Monospace,
                color = Color.White.copy(alpha = 0.9f)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onStartClick,
                modifier = Modifier
                    .width(200.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 12.dp
                )
            ) {
                Text(
                    text = "PRADĖTI",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }

            Button(
                onClick = { showRulesDialog = true },
                modifier = Modifier
                    .width(200.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0B41BB),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 12.dp
                )
            ) {
                Text(
                    text = "KAIP ŽAISTI",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }

    if (showRulesDialog) {
        RulesDialog(onDismissRequest = { showRulesDialog = false })
    }
}


@Composable
fun RulesDialog(onDismissRequest: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismissRequest,

        containerColor = Color(0xFF0D0D0D).copy(alpha = 0.92f),
        titleContentColor = Color.White,
        textContentColor = Color(0xFFD0D0D0),
        shape = RoundedCornerShape(24.dp),

        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "KAIP ŽAISTI",
                    fontSize = 24.sp, // truputį mažesnis
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White,
                    letterSpacing = 2.sp
                )
            }
        },

        text = {
            val scrollState = rememberScrollState()

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    // svarbiausia – ribojam aukštį ir leidžiam scroll
                    .heightIn(max = 260.dp)
                    .verticalScroll(scrollState)
            ) {
                Text(
                    text = "Prieš pradedant pasirink laikmatį ir kategoriją. " +
                            "Jeigu laikmatis nustatytas į 0 – turėsite neribotą laiką (∞).",
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Monospace
                )

                Text(
                    text = "Žaidimo tikslas – per nustatytą laiką atspėti kuo daugiau žodžių.",
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Monospace
                )

                Text(
                    text =
                        "1) Laikyk telefoną horizontalioje padėtyje prie kaktos.\n" +
                                "2) Kiti žmonės turi paaiškinti žodį jo nepasakydami.\n" +
                                "3) Atspėjus žodį – palenk telefoną žemyn (✓ Teisingai).\n" +
                                "4) Norint praleisti arba aiškinantiesiems suklydus – palenk telefoną aukštyn (✗ Praleisti).",
                    fontSize = 15.sp,
                    lineHeight = 18.sp,
                    fontFamily = FontFamily.Monospace
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "💡 Susikurkite savo taisykles ir žaiskite taip, kaip jums smagiausia!",
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFFB0B0B0),
                    lineHeight = 18.sp
                )
            }
        },

        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text(
                    text = "Supratau",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50),
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    )
}







@Preview
@Composable
fun GameStartPreview(){
    GameStartScreen(
        onStartClick = {}
    )
}
