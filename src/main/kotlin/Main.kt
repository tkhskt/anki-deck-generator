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
            cardSeparator = "\$break\$",
            frontBackSeparator = 'å'
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
                val query = Dictionary.Query(
                    keyword = it[0],
                    partOfSpeech = it.getOrNull(1)?.let { Dictionary.PartOfSpeech.find(it) }
                )
                val entry = dictionary.find(query)
                println("Found: ${it.first()}")
                entries.addAll(entry)
            }
        }.awaitAll()
    }
    return entries
}
