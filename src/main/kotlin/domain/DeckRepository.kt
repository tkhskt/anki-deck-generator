package com.tkhskt.ankideckgenerator.domain

interface DeckRepository {
    suspend fun save(deck: Deck, filePath: String)
}
