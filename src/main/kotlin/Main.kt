package com.tkhskt.ankideckgenerator

import com.tkhskt.ankideckgenerator.dictionary.Dictionary
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    measure {
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
}

private suspend fun generateEntries(
    inputFilePath: String,
    dictionaryPath: String,
): List<Dictionary.Entry> {
    val input = CsvLoader(inputFilePath).read()
    val dictionary: Dictionary = Dictionary.eijiro(dictionaryPath)
    val queries = input.map { query ->
        Dictionary.Query(
            keyword = query[0],
            partOfSpeech = query.getOrNull(1)?.let { Dictionary.PartOfSpeech.find(it) }
        )
    }
    val entries = dictionary.findAll(queries)
    return entries
}

private suspend fun measure(block: suspend () -> Unit) {
    val start = System.currentTimeMillis()
    block()
    val end = System.currentTimeMillis()
    println("Execution Time: ${end - start}ms")
}
