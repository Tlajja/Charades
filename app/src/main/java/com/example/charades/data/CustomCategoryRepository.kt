package com.example.charades.data

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class CustomCategoryData(
    val displayName: String,
    val words: List<String>
)

class CustomCategoryRepository(private val context: Context) {
    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }
    private val categoriesFile = File(context.filesDir, "custom_categories.json")

    fun saveCategories(categories: List<Category.Custom>) {
        try {
            // Convert Category.Custom to serializable data class
            val data = categories.map { CustomCategoryData(it.displayName, it.words) }
            val jsonString = json.encodeToString(data)
            categoriesFile.writeText(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadCategories(): List<Category.Custom> {
        return try {
            if (categoriesFile.exists()) {
                val jsonString = categoriesFile.readText()
                val data = json.decodeFromString<List<CustomCategoryData>>(jsonString)
                // Convert back to Category.Custom
                data.map { Category.Custom(it.displayName, it.words) }
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}