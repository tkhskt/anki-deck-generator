package com.tkhskt.ankideckgenerator.infra

import com.tkhskt.ankideckgenerator.domain.Card
import com.tkhskt.ankideckgenerator.domain.Deck
import com.tkhskt.ankideckgenerator.domain.DeckRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class DeckRepositoryImpl(
    private val cardSeparator: String,
) : DeckRepository {

    override suspend fun save(deck: Deck, filePath: String) {
        withContext(Dispatchers.IO) {
            val csvFile = File(filePath)
            val cards = deck.cards
            csvFile.printWriter().use { writer ->
                cards.forEach { card ->
                    writer.println("${card.front}${Card.SEPARATOR}${card.back}$cardSeparator")
                }
            }
        }
    }
}
