package com.tkhskt.ankideckgenerator.dictionary

import com.tkhskt.ankideckgenerator.dictionary.Dictionary.Entry
import com.tkhskt.ankideckgenerator.dictionary.Dictionary.PartOfSpeech
import java.io.InputStream
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class Eijiro(
    private val filePath: String,
) : Dictionary {

    override suspend fun find(keyword: String, partOfSpeech: PartOfSpeech?): List<Entry> {
        val entries = findEntries(keyword).mergeEntries()
        return entries.filterPartOfSpeech(partOfSpeech)
    }

    private suspend fun findEntries(keyword: String) = suspendCoroutine { cont ->
        val stream = openFile() ?: throw IllegalArgumentException("File not found: $filePath")

        val entries = mutableListOf<Entry>()
        stream.bufferedReader().useLines { lines ->
            lines.forEach { line ->
                if (isTargetEntry(line, keyword)) {
                    val entry = createEntryFrom(line)
                    entries.add(entry)
                }
            }
        }
        if (entries.isNotEmpty()) {
            cont.resume(entries.toList())
        } else {
            cont.resumeWithException(Exception())
        }
    }

    private fun List<Entry>.mergeEntries(): List<Entry> {
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
                partOfSpeech = "【${key}】", // `{}` で括って漢字部分のみ
                definition = groupedEntries.mapIndexed { index, value ->
                    value.copy(
                        definition = "${index + 1}. ${value.definition}"
                    )
                }.joinToString("\n") { it.definition },
                exampleSentence = groupedEntries.firstOrNull { it.exampleSentence != null }?.exampleSentence
            )
        }
    }

    private fun List<Entry>.filterPartOfSpeech(partOfSpeech: PartOfSpeech?): List<Entry> {
        partOfSpeech ?: return this
        return filter {
            it.partOfSpeech?.contains(partOfSpeech.value) ?: false
        }
    }

    private fun openFile(): InputStream? {
        return {}::class.java.classLoader.getResourceAsStream(filePath)
    }

    private fun isTargetEntry(input: String, targetKeyword: String): Boolean {
        val regex = Regex("■$targetKeyword\\s*(\\{.*?\\})?\\s*:(.*)", RegexOption.MULTILINE)
        val matchResult = regex.find(input)
        val isMatchingEntry = matchResult != null && input.contains("【＠】").not()
        return isMatchingEntry
    }

    private fun createEntryFrom(line: String): Entry {
        val regex = Regex("■(.*?)\\s*(\\{.*?\\})?\\s*:(.*)", RegexOption.MULTILINE)

        val matchResult = regex.find(line) ?: throw IllegalArgumentException()
        val keyword = matchResult.groupValues[1].trim() // `■` と `{}` の間の単語
        val partOfSpeech = matchResult.groupValues.getOrNull(2)?.trim() ?: "" // `{}` 内の内容（ない場合は空文字）
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
