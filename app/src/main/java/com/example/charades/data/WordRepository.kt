package com.example.charades.data

import android.content.Context
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable

sealed class Category {
    abstract val displayName: String

    data class Predefined(
        val fileName: String,
        override val displayName: String
    ) : Category()

    data class Custom(
        override val displayName: String,
        val words: List<String>
    ) : Category()

    companion object {
        val predefinedCategories: List<Predefined> = listOf(
            Predefined("animals.json", "Gyvūnai"),
            Predefined("sports.json", "Sportas"),
            Predefined("foods.json", "Maistas"),
            Predefined("music.json", "Muzika"),
            Predefined("people.json", "Žmonės")
        )
    }
}


@Serializable
data class WordList(
    val words: List<String>
)

class WordRepository(private val context: Context) {
    private val json = Json { ignoreUnknownKeys = true }

    fun loadWordsFromCategory(category: Category): List<String> {
        return when (category) {
            is Category.Predefined -> {
                try {
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
            is Category.Custom -> category.words
        }
    }

    fun loadAllWords(): List<String> {
        return Category.predefinedCategories.flatMap { loadWordsFromCategory(it) }
    }

    fun getRandomWord(category: Category? = null): String? {
        return if (category != null) {
            loadWordsFromCategory(category).randomOrNull()
        } else {
            loadAllWords().randomOrNull()
        }
    }
}