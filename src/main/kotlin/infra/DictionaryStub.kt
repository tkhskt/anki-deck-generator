package com.tkhskt.ankideckgenerator.infra

import com.tkhskt.ankideckgenerator.domain.Dictionary

class DictionaryStub : Dictionary {

    override suspend fun find(query: Dictionary.Query): List<Dictionary.Entry> {
        return (0..4).map {
            Dictionary.Entry(
                id = it,
                word = "word $it",
                pronunciation = "pro",
                partOfSpeech = "verb",
                definition = "definition",
                exampleSentence = Dictionary.Entry.ExampleSentence(
                    ja = "japanese sentence",
                    en = "english sentence"
                )
            )
        }
    }

    override suspend fun findAll(queries: List<Dictionary.Query>): List<Dictionary.Entry> {
        return (0..4).map {
            Dictionary.Entry(
                id = it,
                word = "word $it",
                pronunciation = "pro",
                partOfSpeech = "verb",
                definition = "definition",
                exampleSentence = Dictionary.Entry.ExampleSentence(
                    ja = "japanese sentence",
                    en = "english sentence"
                )
            )
        }
    }
}
