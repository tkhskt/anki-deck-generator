package com.tkhskt.ankideckgenerator

suspend fun main() {
    val dictionary = Dictionary("dictionary/eijiro.txt")
    val entry = dictionary.find("first", Dictionary.PartOfSpeech.ADVERB)
    entry.forEach {
        println(it)
    }
}
