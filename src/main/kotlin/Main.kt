package com.tkhskt.ankideckgenerator

import com.tkhskt.ankideckgenerator.dictionary.Dictionary
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    try {
        val input = CsvLoader("input.csv").read()
        val dictionary: Dictionary = Dictionary.eijiro("dictionary/eijiro.txt")
        val entries = mutableListOf<Dictionary.Entry>()
        input.map {
            println("Search: ${it.first()}")
            async {
                val entry = dictionary.find(it[0], it.getOrNull(1)?.let { Dictionary.PartOfSpeech.find(it) })
                entries.addAll(entry)
            }
        }.awaitAll()
        val generator = DeckGenerator(
            fileName = "deck",
            cardSeparator = "tkhskt"
        )
        generator.generate(entries)
    } catch (e: Exception) {
        println(e.toString())
    }
}
