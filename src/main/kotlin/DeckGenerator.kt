package com.tkhskt.ankideckgenerator

import com.tkhskt.ankideckgenerator.dictionary.Dictionary
import java.io.File

class DeckGenerator(
    private val fileName: String,
    private val cardSeparator: String,
    private val frontBackSeparator: Char,
) {

    fun generate(dictionaryEntries: List<Dictionary.Entry>) {
        createDir()
        val csvFile = File("$OUTPUT_DIR/$fileName.csv")
        val data = dictionaryEntries.map {
            listOf(
                "${it.word}\n\n${it.exampleSentence?.en ?: ""}",
                "${parsePartOfSpeech(it.partOfSpeech)}${it.definition}\n\n${it.exampleSentence?.ja ?: ""}${cardSeparator}"
            )
        }

        csvFile.printWriter().use { writer ->
            data.forEach { row ->
                writer.println(row.joinToString(frontBackSeparator.toString()))
            }
        }
    }

    private fun createDir() {
        val directory = File(OUTPUT_DIR)
        if (!directory.exists()) {
            directory.mkdirs()
        }
    }

    private fun parsePartOfSpeech(partOfSpeech: String?): String {
        return partOfSpeech?.let {
            "【$it】\n\n"
        } ?: ""
    }

    companion object {
        private const val OUTPUT_DIR = "out"
    }
}
