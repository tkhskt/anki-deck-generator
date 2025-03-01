package com.tkhskt.ankideckgenerator

import com.tkhskt.ankideckgenerator.dictionary.Dictionary

suspend fun main() {
    val dictionary: Dictionary = Dictionary.eijiro("dictionary/eijiro.txt")
    val entry = dictionary.find("lean")
    val generator = DeckGenerator(
        fileName = "deck",
        cardSeparator = "tkhskt"
    )
    generator.generate(entry)
}
