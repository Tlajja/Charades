package com.example.charades.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.charades.R

@Composable
fun SetPlayersView(
    onBackClick: () -> Unit,
    onStartClick: (List<String>) -> Unit
) {
    val players = remember {
        mutableStateListOf("", "")
    }

    var validationAttempted by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

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


        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(start = 32.dp, end = 32.dp, top = 80.dp, bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Text(
                    text = "Žaidėjai",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = {
                            if (players.size > 2) {
                                players.removeLast()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Sumažinti žadėjų skaičių",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = players.size.toString(),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    IconButton(onClick = {
                        players.add("")
                    }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Padidinti žaidėjų skaičių",
                            tint = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.height(52.dp))
            }

            itemsIndexed(players) { index, player ->
                val isError = validationAttempted && player.isBlank()
                OutlinedTextField(
                    value = player,
                    label = { Text("Žaidėjas ${index + 1}") },
                    onValueChange = { newName -> players[index] = newName },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = isError,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = if (index == players.size - 1) {
                            ImeAction.Done
                        } else {
                            ImeAction.Next
                        }
                    ),
                    keyboardActions = KeyboardActions (
                         onDone = {
                             focusManager.clearFocus()
                         }
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        cursorColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.LightGray,
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.Gray,
                        errorIndicatorColor = Color.Red,
                        errorLabelColor = Color.Red
                    )
                )
            }
        }
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
        Button(
            onClick = {
                validationAttempted = true
                val playerNames = players.filter { it.isNotBlank() }
                if (playerNames.size == players.size && players.size >= 2) {
                    onStartClick(playerNames)
                } },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
                .width(200.dp)
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                text = "Pradėti",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
fun SetPlayersViewPreview() {
    SetPlayersView(
        onBackClick = {},
        onStartClick = {}
    )
}
