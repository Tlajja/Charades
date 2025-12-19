package com.tlajja.charades.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tlajja.charades.R

@Composable
fun GameStartScreen(
    onStartClick: () -> Unit,
    onStatisticsClick: () -> Unit
) {
    var showRulesDialog by remember { mutableStateOf(false) }
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
                            Color.Black.copy(alpha = 0.3f),
                            Color.Black.copy(alpha = 0.6f)
                        )
                    )
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .widthIn(max = 520.dp)
                .align(Alignment.Center)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "ŠARADAI",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = Color.White,
                letterSpacing = 4.sp
            )
            Text(
                text = "Spėk, atspėk ir laimėk!",
                fontSize = 16.sp,
                fontFamily = FontFamily.Monospace,
                color = Color.White.copy(alpha = 0.9f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onStartClick,
                modifier = Modifier
                    .width(200.dp)
                    .height(50.dp),
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
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }

            Button(
                onClick = onStatisticsClick,
                modifier = Modifier
                    .width(200.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF9800),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 12.dp
                )
            ) {
                Text(
                    text = "ISTORIJA",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }

            Button(
                onClick = { showRulesDialog = true },
                modifier = Modifier
                    .width(200.dp)
                    .height(50.dp),
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
                    fontSize = 18.sp,
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
                    fontSize = 24.sp,
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
                    .heightIn(max = 320.dp)
                    .verticalScroll(scrollState)
            ) {
                Text(
                    text = "Prieš pradedant pasirink laikmatį, kategoriją ir žaidimo režimą. " +
                            "Jeigu laikmatis nustatytas į 0 – turėsite neribotą laiką (∞), " +
                            "tad raundas nesibaigs nuo laiko.",
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Monospace
                )

                Text(
                    text = "Žaidimo tikslas – per nustatytą laiką atspėti kuo daugiau žodžių.",
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Monospace
                )

                Text(
                    text = "Režimai:\n" +
                            "• „Vienas žaidėjas“ – žaidžia vienas žmogus ir renka savo taškus.\n" +
                            "• „Keli žaidėjai“ – įveskite kelis žaidėjus, kiekvienas paeiliui žaidžia savo raundą, " +
                            "o pabaigoje rodoma bendra rezultatų lentelė.\n" +
                            "• „Komandos“ – susikurkite 2 ar daugiau komandų ir pasirinkite, kiek raundų žaisite. " +
                            "Komandos žaidžia paeiliui, taškai sumuojami komandai, o pabaigoje rodoma komandų rezultatų lentelė.",
                    fontSize = 15.sp,
                    lineHeight = 18.sp,
                    fontFamily = FontFamily.Monospace
                )


                Text(
                    text = "Jei įjungtas nustatymas „Nekartoti žodžių tarp žaidimų“, " +
                            "programėlė stengsis nerodyti jau matytų žodžių, " +
                            "kol bus panaudota kuo daugiau naujų. " +
                            "Išjungus ir vėl įjungus šį nustatymą, matytų žodžių sąrašas bus atnaujintas.",
                    fontSize = 15.sp,
                    lineHeight = 18.sp,
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
                    text = "Jeigu norite daugiau laisvės – susikurkite savo taisykles:",
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFFB0B0B0),
                    lineHeight = 18.sp
                )
                Text(
                    text = "💡 Galite keisti, kaip skaičiuojami taškai, ar leisti tik gestus, garsus, mimiką ir t.t.",
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

