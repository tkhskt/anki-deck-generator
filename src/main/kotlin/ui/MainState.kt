package com.tkhskt.ankideckgenerator.ui

import com.tkhskt.ankideckgenerator.domain.Card
import com.tkhskt.ankideckgenerator.domain.Dictionary

data class MainState(
    val isDictionaryLoading: Boolean,
    val isSearching: Boolean,
    val keyword: String,
    val selectedPartOfSpeech: Dictionary.PartOfSpeech?,
    val dictionary: Dictionary?,
    val searchResult: List<Card>?,
) {

    val partOfSpeeches = Dictionary.PartOfSpeech.entries

    companion object {
        val Empty = MainState(
            isDictionaryLoading = false,
            isSearching = false,
            keyword = "",
            selectedPartOfSpeech = null,
            dictionary = null,
            searchResult = null
        )
    }
}
