package com.tkhskt.ankideckgenerator.dictionary

interface Dictionary {

    fun find(query: Query): List<Entry>

    fun findAll(queries: List<Query>): List<Entry>

    data class Entry(
        val word: String,
        val pronunciation: String? = null,
        val partOfSpeech: String?,
        val definition: String,
        val exampleSentence: ExampleSentence?,
    ) {
        data class ExampleSentence(
            val en: String,
            val ja: String,
        )
    }

    enum class PartOfSpeech(val value: String) {
        NOUN("名"),
        VERB("動"),
        ADJECTIVE("形"),
        ADVERB("副"),
        AUXILIARY("助"),
        ;

        companion object {
            fun find(value: String): PartOfSpeech? {
                return entries.find { it.value == value }
            }
        }
    }

    data class Query(
        val keyword: String,
        val partOfSpeech: PartOfSpeech? = null,
    )

    companion object {
        fun eijiro(filePath: String): Dictionary {
            return Eijiro(filePath)
        }
    }
}
