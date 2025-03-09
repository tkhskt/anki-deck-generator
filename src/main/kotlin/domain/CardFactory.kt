package com.tkhskt.ankideckgenerator.domain

class CardFactory {

    fun create(entry: Dictionary.Entry): Card {
        return Card(
            front = front(entry),
            back = back(entry)
        )
    }

    private fun front(entry: Dictionary.Entry): String {
        val word = entry.word
        val pronunciation = parsePronunciation(entry.pronunciation)
        val englishExampleSentence = parseEnglishExampleSentence(entry.exampleSentence?.en)
        return "${word}${pronunciation}${englishExampleSentence}"
    }

    private fun back(entry: Dictionary.Entry): String {
        val partOfSpeech = parsePartOfSpeech(entry.partOfSpeech)
        val definition = entry.definition
        val japaneseExampleSentence = parseJapaneseExampleSentence(entry.exampleSentence?.ja)
        return "${partOfSpeech}${definition}${japaneseExampleSentence}"
    }

    private fun parsePronunciation(pronunciation: String?): String {
        return pronunciation?.let {
            "\n\n${it}"
        } ?: ""
    }

    private fun parseEnglishExampleSentence(exampleSentence: String?): String {
        return exampleSentence?.let {
            "\n\n${exampleSentence}"
        } ?: ""
    }

    private fun parsePartOfSpeech(partOfSpeech: String?): String {
        return partOfSpeech?.let {
            "【$it】\n\n"
        } ?: ""
    }

    private fun parseJapaneseExampleSentence(exampleSentence: String?): String {
        return exampleSentence?.let {
            "\n\n${exampleSentence}"
        } ?: ""
    }
}
