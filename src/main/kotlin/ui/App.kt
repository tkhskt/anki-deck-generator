package com.tkhskt.ankideckgenerator.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.tkhskt.ankideckgenerator.domain.CardFactory
import com.tkhskt.ankideckgenerator.infra.CsvLoader
import com.tkhskt.ankideckgenerator.infra.DeckRepositoryImpl
import com.tkhskt.ankideckgenerator.infra.EijiroRepository
import com.tkhskt.ankideckgenerator.ui.page.MainPage
import com.tkhskt.ankideckgenerator.ui.page.MainViewModel

@Composable
fun App() {
    val mainViewModel = remember {
        MainViewModel(
            dictionaryRepository = EijiroRepository("dictionary/eijiro.txt"),
            cardFactory = CardFactory(),
            deckRepository = DeckRepositoryImpl(cardSeparator = "\$break\$"),
            csvLoader = CsvLoader(),
        )
    }
    AnkiDeckGeneratorTheme {
        MainPage(mainViewModel)
    }
}
