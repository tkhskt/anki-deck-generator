package com.tkhskt.ankideckgenerator.ui.page

import com.tkhskt.ankideckgenerator.ui.organization.CardState

data class MainUiState(
    val isDictionaryLoading: Boolean,
    val isSearching: Boolean,
    val keyword: String,
    val selectedPartOfSpeech: String?,
    val partOfSpeeches: List<String>,
    val cards: List<CardState>?,
) {
    val numberOfCards = cards?.size ?: 0

    val isExportButtonEnabled = cards.isNullOrEmpty().not()

    constructor(state: MainState) : this(
        isDictionaryLoading = state.isDictionaryLoading,
        isSearching = state.isSearching,
        keyword = state.keyword,
        selectedPartOfSpeech = state.selectedPartOfSpeech?.value,
        partOfSpeeches = state.partOfSpeeches.map { it.value },
        cards = state.searchResult?.map { CardState(front = it.front, back = it.back) },
    )

    companion object {
        val Empty = MainUiState(
            isDictionaryLoading = false,
            isSearching = false,
            keyword = "",
            selectedPartOfSpeech = null,
            partOfSpeeches = emptyList(),
            cards = null,
        )
    }
}
