package com.tkhskt.ankideckgenerator.ui.page

import com.tkhskt.ankideckgenerator.domain.Card
import com.tkhskt.ankideckgenerator.domain.CardFactory
import com.tkhskt.ankideckgenerator.domain.Deck
import com.tkhskt.ankideckgenerator.domain.Dictionary
import com.tkhskt.ankideckgenerator.infra.CsvLoader
import com.tkhskt.ankideckgenerator.infra.DeckExporter
import com.tkhskt.ankideckgenerator.ui.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val state = MutableStateFlow(MainState.Empty)
    val uiState = state.map { MainUiState(it) }

    fun init() {
        viewModelScope.launch {
            state.update {
                it.copy(
                    isDictionaryLoading = true
                )
            }
            val dictionary = Dictionary.eijiro("dictionary/eijiro.txt")
//            val dictionary = DictionaryStub()
            state.update {
                it.copy(
                    dictionary = dictionary,
                    isDictionaryLoading = false
                )
            }
        }
    }

    fun changeKeyword(keyword: String) {
        if (keyword.isEmpty()) {
            state.update {
                it.copy(
                    keyword = keyword,
                    searchResult = null,
                )
            }
        }
        search(keyword = keyword, partOfSpeech = state.value.selectedPartOfSpeech)
    }

    fun changePartOfSpeech(partOfSpeech: String?) {
        search(
            keyword = state.value.keyword,
            partOfSpeech = partOfSpeech?.let { Dictionary.PartOfSpeech.find(it) }
        )
    }

    fun searchByFile(filePath: String) {
        val input = CsvLoader(filePath).read()
        val queries = input.associate { query ->
            query[0] to query.getOrNull(1)?.let { Dictionary.PartOfSpeech.find(it) }
        }
        viewModelScope.launch {
            searchAll(queries)
        }
    }

    fun exportDeck(filePath: String) {
        val cards = state.value.searchResult ?: return
        val exporter = DeckExporter(
            cardSeparator = "\$break\$",
        )
        val deck = Deck(
            cards = cards
        )
        viewModelScope.launch {
            exporter.export(deck, filePath)
        }
    }

    private fun search(keyword: String, partOfSpeech: Dictionary.PartOfSpeech?) {
        val dictionary = state.value.dictionary ?: return

        state.update {
            it.copy(
                keyword = keyword,
                selectedPartOfSpeech = partOfSpeech,
                isSearching = true,
            )
        }
        viewModelScope.launch {
            val query = Dictionary.Query(
                keyword = keyword,
                partOfSpeech = partOfSpeech,
            )
            val entries = dictionary.find(query)
            val cards = entries.map { entry -> CardFactory().create(entry) }
            state.update {
                it.copy(
                    isSearching = false,
                    searchResult = cards.map { card -> Card(card.front, card.back) },
                )
            }
        }
    }

    private fun searchAll(keywords: Map<String, Dictionary.PartOfSpeech?>) {
        val dictionary = state.value.dictionary ?: return
        state.update { it.copy(isSearching = true) }

        viewModelScope.launch {
            val queries = keywords.map {
                Dictionary.Query(it.key, it.value)
            }
            val entries = dictionary.findAll(queries)
            val cards = entries.map { entry -> CardFactory().create(entry) }
            state.update {
                it.copy(
                    isSearching = false,
                    searchResult = cards.map { card -> Card(card.front, card.back) },
                )
            }
        }
    }
}
