package com.example.charades.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.charades.R
import com.example.charades.data.Category
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions

@Composable
fun CustomCategoryView(
    categories: List<Category.Custom>,
    onSaveNew: (Category.Custom) -> Unit,
    onUpdateCategory: (old: Category.Custom, updated: Category.Custom) -> Unit,
    onDeleteCategory: (Category.Custom) -> Unit,
    onBackClick: () -> Unit
) {
    var categoryName by remember { mutableStateOf("") }
    var currentWord by remember { mutableStateOf("") }
    var words by remember { mutableStateOf<List<String>>(emptyList()) }
    var editingCategory by remember { mutableStateOf<Category.Custom?>(null) }

    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    fun startEditing(category: Category.Custom) {
        editingCategory = category
        categoryName = category.displayName
        words = category.words
        currentWord = ""
    }

    fun clearEditor() {
        editingCategory = null
        categoryName = ""
        words = emptyList()
        currentWord = ""
        focusManager.clearFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
        contentAlignment = Alignment.Center
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
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = {
                    focusManager.clearFocus()
                    onBackClick()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Grįžti",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Savos kategorijos",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(
                        color = Color.Black.copy(alpha = 0.55f),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(20.dp)
                    .verticalScroll(scrollState)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (editingCategory == null)
                            "Sukurti naują kategoriją"
                        else
                            "Redaguojama kategorija",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Monospace,
                        color = Color.White
                    )

                    // Category name
                    TextField(
                        value = categoryName,
                        onValueChange = { categoryName = it },
                        label = { Text("Kategorijos pavadinimas") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Text
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        )
                    )

                    // Add words
                    Column {
                        Text(
                            text = "Pridėti žodžius po vieną:",
                            fontSize = 14.sp,
                            fontFamily = FontFamily.Monospace,
                            color = Color(0xFFD0D0D0)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                value = currentWord,
                                onValueChange = { currentWord = it },
                                label = { Text("Naujas žodis") },
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    imeAction = ImeAction.Next,
                                    keyboardType = KeyboardType.Text
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        val cleaned = currentWord.trim()
                                        if (cleaned.isNotEmpty() && !words.contains(cleaned)) {
                                            words = words + cleaned
                                            currentWord = ""
                                        }
                                    }
                                )
                            )

                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    val cleaned = currentWord.trim()
                                    if (cleaned.isNotEmpty() && !words.contains(cleaned)) {
                                        words = words + cleaned
                                        currentWord = ""
                                    }
                                    focusManager.clearFocus()
                                },
                                enabled = currentWord.trim().isNotEmpty()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Pridėti žodį"
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        if (words.isEmpty()) {
                            Text(
                                text = "Dar nėra pridėtų žodžių.",
                                fontSize = 13.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color.Gray
                            )
                        } else {
                            Text(
                                text = "Žodžiai (${words.size}):",
                                fontSize = 14.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            @OptIn(ExperimentalLayoutApi::class)
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                words.forEach { word ->
                                    CustomWordChip(
                                        text = word,
                                        onRemove = {
                                            words = words.filterNot { it == word }
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Save / clear buttons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedButton(
                            onClick = { clearEditor() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Išvalyti",
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Button(
                            onClick = {
                                val name = categoryName.trim()
                                if (name.isNotEmpty() && words.isNotEmpty()) {
                                    val newCategory = Category.Custom(name, words)
                                    if (editingCategory == null) {
                                        onSaveNew(newCategory)
                                        clearEditor()
                                    } else {
                                        onUpdateCategory(editingCategory!!, newCategory)
                                        clearEditor()
                                    }
                                }
                            },
                            enabled = categoryName.trim().isNotEmpty() && words.isNotEmpty(),
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4CAF50),
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = if (editingCategory == null) "Sukurti" else "Išsaugoti",
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Existing categories
                    Text(
                        text = "Esamos savos kategorijos:",
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Color.White
                    )

                    if (categories.isEmpty()) {
                        Text(
                            text = "Dar nesukūrėte nė vienos kategorijos.",
                            fontSize = 13.sp,
                            fontFamily = FontFamily.Monospace,
                            color = Color.Gray
                        )
                    } else {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            categories.forEach { category ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            Color.Black.copy(alpha = 0.35f),
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = category.displayName,
                                            fontSize = 16.sp,
                                            fontFamily = FontFamily.Monospace,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "Žodžių: ${category.words.size}",
                                            fontSize = 13.sp,
                                            fontFamily = FontFamily.Monospace,
                                            color = Color(0xFFD0D0D0)
                                        )
                                    }
                                    IconButton(onClick = { startEditing(category) }) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Redaguoti",
                                            tint = Color.White
                                        )
                                    }
                                    IconButton(onClick = { onDeleteCategory(category) }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Ištrinti",
                                            tint = Color(0xFFFF6B6B)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomWordChip(
    text: String,
    onRemove: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(
                color = Color.Black.copy(alpha = 0.25f),
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontFamily = FontFamily.Monospace,
            color = Color.White
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Pašalinti",
            tint = Color(0xFFFFB3B3),
            modifier = Modifier
                .size(16.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onRemove() }
        )
    }
}
