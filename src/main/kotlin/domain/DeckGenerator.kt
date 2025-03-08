package com.tkhskt.ankideckgenerator.domain

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
                front(entry = it),
                back(entry = it)
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

    private fun front(entry: Dictionary.Entry): String {
        return "${entry.word}\n\n${parsePronunciation(entry.pronunciation)} ${entry.exampleSentence?.en ?: ""}"
    }

    private fun back(entry: Dictionary.Entry): String {
        return "${parsePartOfSpeech(entry.partOfSpeech)}${entry.definition}\n\n${entry.exampleSentence?.ja ?: ""}${cardSeparator}"
    }

    private fun parsePronunciation(pronunciation: String?): String {
        return pronunciation?.let {
            "${it}\n\n"
        } ?: ""
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
