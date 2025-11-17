package com.example.charades.data

import android.content.Context
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable

enum class Category(val fileName: String, val displayName: String) {
    ANIMALS("animals.json", "Gyvūnai"),
    SPORTS("sports.json", "Sportas"),
    FOOD("food.json", "Maistas"),
    MUSIC("music.json", "Muzika"),
    PEOPLE("people.json", "Žmonės")
}

@Serializable
data class WordList(
    val words: List<String>
)

class WordRepository(private val context: Context) {
    private val json = Json { ignoreUnknownKeys = true }

    fun loadWordsFromCategory(category: Category): List<String> {
        return try {
            val jsonString = context.assets
                .open(category.fileName)
                .bufferedReader()
                .use { it.readText() }

            val wordList = json.decodeFromString<WordList>(jsonString)
            wordList.words
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun loadAllWords(): List<String> {
        return Category.entries.flatMap { loadWordsFromCategory(it) }
    }

    fun getRandomWord(category: Category? = null): String? {
        return if (category != null) {
            loadWordsFromCategory(category).randomOrNull()
        } else {
            loadAllWords().randomOrNull()
        }
    }
}