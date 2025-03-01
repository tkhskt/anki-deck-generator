package com.tkhskt.ankideckgenerator

import com.tkhskt.ankideckgenerator.dictionary.Dictionary
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    try {
        val entries = generateEntries(
            inputFilePath = "input.csv",
            dictionaryPath = "dictionary/eijiro.txt"
        )
        val generator = DeckGenerator(
            fileName = "deck",
            cardSeparator = "tkhskt"
        )
        generator.generate(entries)
    } catch (e: Exception) {
        println(e.toString())
    }
}

private suspend fun generateEntries(
    inputFilePath: String,
    dictionaryPath: String,
): List<Dictionary.Entry> {
    val input = CsvLoader(inputFilePath).read()
    val dictionary: Dictionary = Dictionary.eijiro(dictionaryPath)
    val entries = mutableListOf<Dictionary.Entry>()
    coroutineScope {
        input.map {
            async {
                println("Search: ${it.first()}")
                val entry = dictionary.find(it[0], it.getOrNull(1)?.let { Dictionary.PartOfSpeech.find(it) })
                entries.addAll(entry)
            }
        }.awaitAll()
    }
    return entries
}
