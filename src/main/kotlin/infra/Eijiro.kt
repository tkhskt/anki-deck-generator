package com.tkhskt.ankideckgenerator.infra

import com.tkhskt.ankideckgenerator.domain.Dictionary
import com.tkhskt.ankideckgenerator.domain.Dictionary.Entry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class Eijiro(
    private val chunkedEntries: List<Sequence<Entry>>,
) : Dictionary {

    override suspend fun find(query: Dictionary.Query): List<Entry> {
        return findAll(listOf(query))
    }

    override suspend fun findAll(queries: List<Dictionary.Query>): List<Entry> {
        val entriesMap = queries.associateWith { mutableListOf<Entry>() }
        search { entry, mutex ->
            queries.forEach { query ->
                if (isTargetEntry(entry, query).not()) return@forEach
                mutex.withLock {
                    val entries = entriesMap[query]
                    entries?.add(entry)
                }
            }
        }
        return entriesMap.map { (_, entries) ->
            entries
                .sortById()
                .mergeSameWordEntries(pronunciation = extractPronunciation(entries))
                .filterNotPronunciationEntry()
        }.flatten()
    }

    private suspend fun search(action: suspend (Entry, Mutex) -> Unit) {
        val mutex = Mutex()
        coroutineScope {
            chunkedEntries.map { entries ->
                async(Dispatchers.Default) {
                    entries.forEach { action(it, mutex) }
                }
            }.awaitAll()
        }
    }

    private fun isTargetEntry(entry: Entry, query: Dictionary.Query): Boolean {
        val isMatchingWord = entry.word == query.keyword
        val isMatchingPartOfSpeech = query.partOfSpeech?.let {
            entry.partOfSpeech?.contains(it.value) ?: true
        } ?: true
        return isMatchingWord && isMatchingPartOfSpeech
    }

    private fun List<Entry>.sortById(): List<Entry> = sortedBy { it.id }

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
                id = groupedEntries.first().id,
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

    private fun extractPronunciation(entries: List<Entry>): String? {
        val pronunciationIncludedEntry = entries.firstOrNull { entry ->
            entry.definition.contains("【発音】")
        } ?: return null
        val regex = """【発音】([^、]+)""".toRegex()
        val definition = pronunciationIncludedEntry.definition
        return regex.find(definition)?.groupValues?.get(1)
    }
}
