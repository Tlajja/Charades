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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.charades.R
import com.example.charades.data.Category
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalLayoutApi::class)
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
    var categoryToDelete by remember { mutableStateOf<Category.Custom?>(null) }

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
            }
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {

            Spacer(modifier = Modifier.height(64.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Editor Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Black.copy(alpha = 0.6f)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (editingCategory == null) "Nauja kategorija" else "Redaguoti",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = Color.White
                            )
                            if (editingCategory != null) {
                                IconButton(onClick = { clearEditor() }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Atšaukti",
                                        tint = Color.White.copy(alpha = 0.7f)
                                    )
                                }
                            }
                        }

                        // Category Name Input
                        OutlinedTextField(
                            value = categoryName,
                            onValueChange = { categoryName = it },
                            label = { Text("Pavadinimas", fontFamily = FontFamily.Monospace) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color(0xFF4CAF50),
                                unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                                focusedLabelColor = Color(0xFF4CAF50),
                                unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                                cursorColor = Color.White
                            ),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done,
                                keyboardType = KeyboardType.Text
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { focusManager.clearFocus() }
                            )
                        )

                        // Word Input
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = currentWord,
                                onValueChange = { currentWord = it },
                                label = { Text("Pridėti žodį", fontFamily = FontFamily.Monospace) },
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = Color(0xFF4CAF50),
                                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                                    focusedLabelColor = Color(0xFF4CAF50),
                                    unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                                    cursorColor = Color.White
                                ),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    imeAction = if (currentWord.isEmpty()) ImeAction.Done else ImeAction.Next,
                                    keyboardType = KeyboardType.Text
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        val cleaned = currentWord.trim()
                                        if (cleaned.isNotEmpty() && !words.contains(cleaned)) {
                                            words = words + cleaned
                                            currentWord = ""
                                        }
                                    },
                                    onDone = { focusManager.clearFocus() }
                                )
                            )

                            IconButton(
                                onClick = {
                                    val cleaned = currentWord.trim()
                                    if (cleaned.isNotEmpty() && !words.contains(cleaned)) {
                                        words = words + cleaned
                                        currentWord = ""
                                    }
                                },
                                enabled = currentWord.trim().isNotEmpty(),
                                modifier = Modifier
                                    .size(56.dp)
                                    .background(
                                        color = if (currentWord.trim().isNotEmpty())
                                            Color(0xFF4CAF50)
                                        else
                                            Color.Gray.copy(alpha = 0.3f),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Pridėti",
                                    tint = Color.White
                                )
                            }
                        }

                        // Words Display - Column Layout
                        if (words.isNotEmpty()) {
                            Divider(color = Color.White.copy(alpha = 0.2f))

                            Text(
                                text = "Žodžiai (${words.size})",
                                fontSize = 14.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color.White.copy(alpha = 0.8f)
                            )

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                words.forEach { word ->
                                    WordRow(
                                        text = word,
                                        onRemove = { words = words.filterNot { it == word } }
                                    )
                                }
                            }
                        }

                        // Action Buttons
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (editingCategory != null || categoryName.isNotEmpty() || words.isNotEmpty()) {
                                OutlinedButton(
                                    onClick = { clearEditor() },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = Color.White
                                    ),
                                    border = ButtonDefaults.outlinedButtonBorder.copy(
                                        width = 1.dp
                                    )
                                ) {
                                    Text("Išvalyti", fontFamily = FontFamily.Monospace)
                                }
                            }

                            Button(
                                onClick = {
                                    val name = categoryName.trim()
                                    if (name.isNotEmpty() && words.isNotEmpty()) {
                                        val newCategory = Category.Custom(name, words)
                                        if (editingCategory == null) {
                                            onSaveNew(newCategory)
                                        } else {
                                            onUpdateCategory(editingCategory!!, newCategory)
                                        }
                                        clearEditor()
                                    }
                                },
                                enabled = categoryName.trim().isNotEmpty() && words.isNotEmpty(),
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF4CAF50),
                                    contentColor = Color.White,
                                    disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = if (editingCategory == null) "Sukurti" else "Išsaugoti",
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Existing Categories
                if (categories.isNotEmpty()) {
                    Text(
                        text = "Mano kategorijos (${categories.size})",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = Color.White,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    categories.forEach { category ->
                        CategoryCard(
                            category = category,
                            onEdit = { startEditing(category) },
                            onDelete = { categoryToDelete = category }
                        )
                    }
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Black.copy(alpha = 0.4f)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Dar nesukūrėte nė vienos kategorijos",
                                fontSize = 14.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color.White.copy(alpha = 0.6f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // BACK BUTTON
        IconButton(
            onClick = {
                focusManager.clearFocus()
                onBackClick()
            },
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

    // Delete Confirmation Dialog
    if (categoryToDelete != null) {
        AlertDialog(
            onDismissRequest = { categoryToDelete = null },
            containerColor = Color(0xFF0D0D0D).copy(alpha = 0.95f),
            titleContentColor = Color.White,
            textContentColor = Color(0xFFD0D0D0),
            shape = RoundedCornerShape(24.dp),
            title = {
                Text(
                    text = "Ištrinti kategoriją?",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Ar tikrai norite ištrinti kategoriją \"${categoryToDelete!!.displayName}\"?",
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "Šio veiksmo negalima atšaukti.",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                        color = Color(0xFFB0B0B0)
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteCategory(categoryToDelete!!)
                        categoryToDelete = null
                    }
                ) {
                    Text(
                        text = "Ištrinti",
                        color = Color(0xFFFF6B6B),
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { categoryToDelete = null }) {
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
private fun CategoryCard(
    category: Category.Custom,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = category.displayName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White
                )
                Text(
                    text = "${category.words.size} žodžių",
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFFB0B0B0)
                )
            }

            IconButton(onClick = onEdit) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Redaguoti",
                    tint = Color(0xFF4CAF50)
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Ištrinti",
                    tint = Color(0xFFFF6B6B)
                )
            }
        }
    }
}

@Composable
private fun WordRow(
    text: String,
    onRemove: () -> Unit
) {
    Surface(
        color = Color(0xFF4CAF50).copy(alpha = 0.15f),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = text,
                fontSize = 15.sp,
                fontFamily = FontFamily.Monospace,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Pašalinti",
                    tint = Color(0xFFFF6B6B),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}


@Preview
@Composable
fun CustomCategoryViewPreview(){
    CustomCategoryView(
        categories = listOf(
            Category.Custom("Mano žodžiai", listOf("Vienas", "Du", "Trys")),
            Category.Custom("Miestai", listOf("Vilnius", "Kaunas"))
        ),
        onSaveNew = {},
        onUpdateCategory = { _, _ -> },
        onDeleteCategory = {},
        onBackClick = {}
    )
}