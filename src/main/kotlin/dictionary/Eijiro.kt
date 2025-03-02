package com.tkhskt.ankideckgenerator.dictionary

import com.tkhskt.ankideckgenerator.dictionary.Dictionary.Entry
import java.io.InputStream

class Eijiro(
    private val filePath: String,
) : Dictionary {

    override fun find(query: Dictionary.Query): List<Entry> {
        val entries = mutableListOf<Entry>()
        search { entry ->
            if (isTargetEntry(entry, query)) {
                entries.add(entry)
            }
        }
        if (entries.isEmpty()) {
            throw Exception("${query.keyword} not found")
        }

        return entries.mergeSameWordEntries(
            pronunciation = extractPronunciation(entries)
        ).filterNotPronunciationEntry()
    }

    override fun findAll(queries: List<Dictionary.Query>): List<Entry> {
        val entriesMap = mutableMapOf<Dictionary.Query, MutableList<Entry>>()
        search { entry ->
            queries.forEach { query ->
                if (isTargetEntry(entry, query)) {
                    val entries = entriesMap.getOrPut(query) { mutableListOf() }
                    entries.add(entry)
                }
            }
        }
        return entriesMap.map { (_, entries) ->
            entries.mergeSameWordEntries(
                pronunciation = extractPronunciation(entries)
            )
        }.flatten().filterNotPronunciationEntry()
    }

    private fun search(action: (Entry) -> Unit) {
        val stream = openFile() ?: throw IllegalArgumentException("File not found: $filePath")
        stream.bufferedReader().useLines { lines ->
            lines.forEach { line ->
                action(createEntryFrom(line))
            }
        }
    }

    private fun openFile(): InputStream? {
        return {}::class.java.classLoader.getResourceAsStream(filePath)
    }

    private fun createEntryFrom(line: String): Entry {
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
            word = keyword,
            partOfSpeech = partOfSpeech,
            definition = definition.split("■・").first(),
            exampleSentence = extractExampleSentence(definition)
        )
    }

    private fun isTargetEntry(entry: Entry, query: Dictionary.Query): Boolean {
        val isMatchingWord = entry.word == query.keyword
        val isMatchingPartOfSpeech = query.partOfSpeech?.let {
            entry.partOfSpeech?.contains(it.value) ?: true
        } ?: true
        return isMatchingWord && isMatchingPartOfSpeech
    }

    private fun List<Entry>.mergeSameWordEntries(pronunciation: String?): List<Entry> {
        val bracketContentRegex = Regex("\\{([^}]*)\\}") // `{}` 内のテキストを抽出
        val kanjiRegex = Regex("\\p{Script=Han}+") // 漢字のみを抽出

        val grouped = groupBy { entry ->
            val bracketMatch = entry.partOfSpeech?.let {
                bracketContentRegex.find(it)?.groupValues?.get(1) // `{}` 内の内容取得
            }
            bracketMatch?.let { kanjiRegex.find(it)?.value } // 漢字部分を取得
        }

        return grouped.mapNotNull { (key, groupedEntries) ->
            Entry(
                word = groupedEntries.first().word, // 先頭の `word` を採用
                partOfSpeech = key,
                pronunciation = pronunciation,
                definition = groupedEntries.mapIndexed { index, value ->
                    value.copy(
                        definition = "${index + 1}. ${value.definition}"
                    )
                }.joinToString("\n") { it.definition },
                exampleSentence = groupedEntries.firstOrNull { it.exampleSentence != null }?.exampleSentence
            )
        }
    }

    private fun List<Entry>.filterNotPronunciationEntry(): List<Entry> {
        return mapNotNull { entry ->
            if (entry.definition.contains("【＠】")) {
                null
            } else {
                entry
            }
        }
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

    private fun extractPronunciation(entries: List<Entry>): String? {
        val pronunciationIncludedEntry = entries.firstOrNull { entry ->
            entry.definition.contains("【発音】")
        } ?: return null
        val regex = """【発音】([^、]+)""".toRegex()
        val definition = pronunciationIncludedEntry.definition
        return regex.find(definition)?.groupValues?.get(1)
    }

    private fun Char.isFullWidthChar(): Boolean {
        return this.code > 0xFF
    }
}
