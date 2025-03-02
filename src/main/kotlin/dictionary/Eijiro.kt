package com.tkhskt.ankideckgenerator.dictionary

import com.tkhskt.ankideckgenerator.dictionary.Dictionary.Entry
import java.io.InputStream

class Eijiro(
    private val filePath: String,
) : Dictionary {

    private val rawEntries: List<Entry>

    init {
        println("Dictionary Loading...")
        val stream = openFile() ?: throw IllegalArgumentException("File not found: $filePath")
        val entries = mutableListOf<Entry>()
        stream.bufferedReader().useLines { lines ->
            lines.forEach { line ->
                entries.add(createEntryFrom(line))
            }
        }
        rawEntries = entries.toList()
        println("Loading Complete")
    }

    override fun find(query: Dictionary.Query): List<Entry> {
        val entries = mutableListOf<Entry>()
        rawEntries.forEach { entry ->
            if (isTargetEntry(entry, query)) {
                entries.add(entry)
            }
        }
        if (entries.isEmpty()) {
            throw Exception("${query.keyword} not found")
        }
        return entries.sortedBy { it.partOfSpeech }.mergeSameWordEntries()
    }

    override fun findAll(queries: List<Dictionary.Query>): List<Entry> {
        val entriesMap = mutableMapOf<Dictionary.Query, List<Entry>>()
        rawEntries.forEach { entry ->
            queries.forEach { query ->
                if (isTargetEntry(entry, query)) {
                    val entries = entriesMap.getOrPut(query) { listOf() }
                    entriesMap[query] = entries.toMutableList().apply {
                        add(entry)
                    }
                }
            }
        }
        return entriesMap.map { (_, entries) ->
            entries.sortedBy { it.partOfSpeech }.mergeSameWordEntries()
        }.flatten()
    }

    private fun List<Entry>.mergeSameWordEntries(): List<Entry> {
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
                definition = groupedEntries.mapIndexed { index, value ->
                    value.copy(
                        definition = "${index + 1}. ${value.definition}"
                    )
                }.joinToString("\n") { it.definition },
                exampleSentence = groupedEntries.firstOrNull { it.exampleSentence != null }?.exampleSentence
            )
        }
    }

    private fun openFile(): InputStream? {
        return {}::class.java.classLoader.getResourceAsStream(filePath)
    }

    private fun isTargetEntry(entry: Entry, query: Dictionary.Query): Boolean {
        val isMatchingWord = entry.word == query.keyword && entry.definition.contains("【＠】").not()
        val isMatchingPartOfSpeech = query.partOfSpeech?.let {
            entry.partOfSpeech?.contains(it.value) ?: true
        } ?: true
        return isMatchingWord && isMatchingPartOfSpeech
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

    private fun extractExampleSentence(wordDefinition: String): Entry.ExampleSentence? {
        val regex = Regex("■・(.*?\\.)\\s*(.*)")

        val matchResult = regex.find(wordDefinition) ?: return null

        return Entry.ExampleSentence(
            en = matchResult.groupValues[1].trim(),
            ja = matchResult.groupValues[2].trim()
        )
    }
}
