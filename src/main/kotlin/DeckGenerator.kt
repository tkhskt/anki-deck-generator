package com.tkhskt.ankideckgenerator

import com.tkhskt.ankideckgenerator.dictionary.Dictionary
import java.io.File

class DeckGenerator(
    private val fileName: String,
    private val cardSeparator: String,
) {

    fun generate(dictionaryEntries: List<Dictionary.Entry>) {
        createDir()
        val csvFile = File("$OUTPUT_DIR/$fileName.csv")
        val data = dictionaryEntries.map {
            listOf(
                "${it.word}\n\n${it.exampleSentence?.en ?: ""}",
                "${it.partOfSpeech}\n\n${it.definition}\n\n${it.exampleSentence?.ja ?: ""}${cardSeparator}"
            )
        }

        csvFile.printWriter().use { writer ->
            data.forEach { row ->
                writer.println(row.joinToString(","))
            }
        }
    }

    private fun createDir() {
        val directory = File(OUTPUT_DIR)
        if (!directory.exists()) {
            directory.mkdirs()
        }
    }

    companion object {
        private const val OUTPUT_DIR = "out"
    }
}
