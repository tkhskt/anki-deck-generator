package com.tkhskt.ankideckgenerator.infra

import com.tkhskt.ankideckgenerator.domain.Dictionary
import com.tkhskt.ankideckgenerator.domain.Dictionary.Entry
import com.tkhskt.ankideckgenerator.domain.DictionaryLoader
import java.io.InputStream

class EijiroLoader(
    private val filePath: String
) : DictionaryLoader {
    private var chunkedEntries: List<Sequence<Entry>> = emptyList()

    override fun load(): Dictionary {
        println("Dictionary Loading...")
        val stream = openFile() ?: throw IllegalArgumentException("File not found: $filePath")
        val entries = mutableListOf<Entry>()
        stream.bufferedReader().useLines { lines ->
            lines.forEachIndexed { index, line ->
                entries.add(createEntryFrom(index, line))
            }
        }
        val numberOfSplits = entries.size / 10
        chunkedEntries = entries.chunked(numberOfSplits).map {
            it.asSequence()
        }
        println("Loading Complete")
        return Eijiro(chunkedEntries)
    }

    private fun openFile(): InputStream? {
        return {}::class.java.classLoader.getResourceAsStream(filePath)
    }

    private fun createEntryFrom(id: Int, line: String): Entry {
        val regex = Regex("■(.*?)\\s*(\\{.*?\\})?\\s*:(.*)", RegexOption.MULTILINE)

        val matchResult = regex.find(line) ?: throw IllegalArgumentException()
        val keyword = matchResult.groupValues[1].trim() // `■` と `{}` の間の単語

        val bracketPart = matchResult.groupValues.getOrNull(2)?.trim() // `{}` 内の内容
        val partOfSpeech = if (bracketPart == null || bracketPart.none { it in '一'..'龯' }) {
            null
        } else {
            bracketPart
        }

        val definition = matchResult.groupValues.getOrNull(3)?.trim() ?: error("") // `:` の後のテキスト

        return Entry(
            id = id,
            word = keyword,
            partOfSpeech = partOfSpeech,
            definition = definition.split("■・").first(),
            exampleSentence = extractExampleSentence(definition)
        )
    }

    private fun extractExampleSentence(wordDefinition: String): Entry.ExampleSentence? {
        // 例文の部分を取得
        val regex = Regex("■・([^■]+)")
        val match = regex.find(wordDefinition) ?: return null

        val sentence = match.groupValues[1].trim()

        val splitIndex = sentence.indexOfFirst { it.isFullWidthChar() }
        return if (splitIndex != -1) {
            val english = sentence.substring(0, splitIndex).trim()
            val japanese = sentence.substring(splitIndex).trim()
            Entry.ExampleSentence(
                en = english,
                ja = japanese
            )
        } else {
            null
        }
    }

    private fun Char.isFullWidthChar(): Boolean {
        return this.code > 0xFF
    }
}
