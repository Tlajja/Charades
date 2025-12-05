package com.example.charades.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.charades.R
import com.example.charades.data.Category
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameSettingsView(
    timerValue: Int,
    selectedCategory: Category?,
    customCategories: List<Category.Custom>,
    onTimerChange: (Int) -> Unit,
    onCategoryChange: (Category?) -> Unit,
    onManageCustomCategoriesClick: () -> Unit,
    soundEnabled: Boolean,
    vibrationEnabled: Boolean,
    onVibrationChange: (Boolean) -> Unit,
    onSoundChange: (Boolean) -> Unit,
    dontRepeatWords: Boolean,
    onDontRepeatWordsChange: (Boolean) -> Unit,
    onStartClick: () -> Unit,
    onBackClick: () -> Unit,
    onSetPlayersClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    val vibrationTooltipState = rememberTooltipState(isPersistent = true)
    val soundTooltipState = rememberTooltipState(isPersistent = true)
    val dontRepeatTooltipState = rememberTooltipState(isPersistent = true)
    val modesTooltipState = rememberTooltipState(isPersistent = true)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        // Background
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

        // Content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(32.dp, 64.dp, 32.dp, 0.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "NUSTATYMAI",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(48.dp))

            // TIMER
            Text(
                text = "LAIKMATIS (sekundės)",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.Monospace,
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.fillMaxWidth()
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
                            onTimerChange(0) // 0 = ∞
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
                        onTimerChange(15)
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

            // CATEGORIES
            Text(
                text = "KATEGORIJOS",
                fontSize = 18.sp,
                fontFamily = FontFamily.Monospace,
                color = Color.White.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(16.dp))

            @OptIn(ExperimentalLayoutApi::class)
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

                Category.predefinedCategories.forEach { category ->
                    CategoryButton(
                        text = category.displayName,
                        isSelected = selectedCategory == category,
                        onClick = { onCategoryChange(category) }
                    )
                }

                customCategories.forEach { custom ->
                    CategoryButton(
                        text = custom.displayName,
                        isSelected = selectedCategory == custom,
                        onClick = { onCategoryChange(custom) },
                        isCustom = true,
                        leadingIcon = Icons.Default.Star,
                        iconDescription = "Custom category"
                    )
                }
            }




            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onManageCustomCategoriesClick,
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.7f)),
                shape = RoundedCornerShape(50),
            ) {
                Text(
                    text = "Tvarkyti savas kategorijas",
                    fontFamily = FontFamily.Monospace
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // VIBRATION
            TooltipBox(
                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                tooltip = {
                    PlainTooltip {
                        Text(
                            "Įjungus vibraciją, telefonas vibruos, kai pažymite teisingą ar praleistą žodį " +
                                    "arba pasibaigia laikas. Tai suteikia fizinį grįžtamąjį ryšį žaidimo metu."
                        )
                    }
                },
                state = vibrationTooltipState,
                enableUserInput = false
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "Vibracija",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                coroutineScope.launch { vibrationTooltipState.show() }
                            }
                    )
                    Switch(
                        checked = vibrationEnabled,
                        onCheckedChange = { onVibrationChange(it) }
                    )
                }
            }

            // SOUND
            TooltipBox(
                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                tooltip = {
                    PlainTooltip {
                        Text(
                            "Įjungus garsus, skambės efektai už teisingus, praleistus žodžius ir žaidimo pabaigą. " +
                                    "Išjungus – žaidimas vyks tyliai be garso efektų."
                        )
                    }
                },
                state = soundTooltipState,
                enableUserInput = false
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "Garsai",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                coroutineScope.launch { soundTooltipState.show() }
                            }
                    )
                    Switch(
                        checked = soundEnabled,
                        onCheckedChange = { onSoundChange(it) }
                    )
                }
            }

            // DONT REPEAT WORDS
            TooltipBox(
                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                tooltip = {
                    PlainTooltip {
                        Text(
                            "Įjungus šį nustatymą, žodžiai bus stengiamasi nerodyti pakartotinai tarp žaidimų, " +
                                    "kol bus panaudota kuo daugiau žodžių iš rinkinio.\n\n" +
                                    "Iš naujo nustatyti matytus žodžius galima išjungus ir vėl įjungus šį nustatymą."
                        )
                    }
                },
                state = dontRepeatTooltipState,
                enableUserInput = false
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "Nekartoti žodžių tarp žaidimų*",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                coroutineScope.launch { dontRepeatTooltipState.show() }
                            }
                    )
                    Switch(
                        checked = dontRepeatWords,
                        onCheckedChange = { onDontRepeatWordsChange(it) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // MODES
            TooltipBox(
                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                tooltip = {
                    PlainTooltip {
                        Text(
                            "„Vienas žaidėjas“ – žaidžia vienas žmogus su telefonu, renka savo taškus.\n\n" +
                                    "„Keli žaidėjai“ – galite įvesti kelis žaidėjus. Kiekvienas žaidėjas paeiliui " +
                                    "žaidžia savo raundą, o po visų raundų parodoma bendra rezultatų lentelė.\n\n" +
                                    "Visi nustatymai (laikmatis, kategorija, vibracija, garsai) taikomi abiem režimams."
                        )
                    }
                },
                state = modesTooltipState,
                enableUserInput = false
            ) {
                Text(
                    text = "REŽIMAI",
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            coroutineScope.launch { modesTooltipState.show() }
                        }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // MODE BUTTONS
            Row {
                Button(
                    onClick = onStartClick,
                    modifier = Modifier
                        .width(200.dp)
                        .height(60.dp)
                        .weight(0.4f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Text(
                        text = "Vienas žaidėjas",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(Modifier.weight(0.02f))
                Button(
                    onClick = onSetPlayersClick,
                    modifier = Modifier
                        .width(200.dp)
                        .height(60.dp)
                        .weight(0.4f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD74E23),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Text(
                        text = "Keli žaidėjai",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

        // BACK BUTTON
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
fun CategoryButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    isCustom: Boolean = false,
    leadingIcon: ImageVector? = null,
    iconDescription: String? = null
) {
    val containerColor =
        if (isSelected) {
            if (isCustom) Color(0xFFAD4AFF)  // Custom selected → purple
            else Color(0xFF4CAF50)          // Normal selected → green
        } else {
            Color.Transparent
        }

    val contentColor =
        if (isSelected) Color.White else Color.White.copy(alpha = 0.9f)

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        border = if (!isSelected)
            BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
        else null,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = iconDescription,
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(text, fontFamily = FontFamily.Monospace)
        }
    }
}



@Preview
@Composable
fun GameSettingsPreview() {
    GameSettingsView(
        timerValue = 60,
        selectedCategory = null,
        customCategories = listOf(
            Category.Custom("Mano žodžiai", listOf("Vienas", "Du"))
        ),
        onTimerChange = {},
        onCategoryChange = {},
        onManageCustomCategoriesClick = {},
        soundEnabled = true,
        vibrationEnabled = true,
        onVibrationChange = {},
        onSoundChange = {},
        dontRepeatWords = true,
        onDontRepeatWordsChange = {},
        onStartClick = {},
        onBackClick = {},
        onSetPlayersClick = {}
    )
}
