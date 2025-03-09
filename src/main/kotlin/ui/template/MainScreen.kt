package com.tkhskt.ankideckgenerator.ui.template

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tkhskt.ankideckgenerator.ui.MainUiState
import com.tkhskt.ankideckgenerator.ui.organization.Card
import com.tkhskt.ankideckgenerator.ui.organization.FileDialog
import com.tkhskt.ankideckgenerator.ui.organization.Mode
import com.tkhskt.ankideckgenerator.ui.organization.SearchForm
import com.tkhskt.ankideckgenerator.ui.organization.SearchResultHeader

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    uiState: MainUiState,
    onKeywordChange: (String) -> Unit,
    onPartOfSpeechChange: (String?) -> Unit,
    onInputFilePathSelect: (filePath: String) -> Unit,
    onExportFilePathSelect: (filePath: String) -> Unit,
) {

    var isInputFileChooserOpen by remember { mutableStateOf(false) }
    if (isInputFileChooserOpen) {
        FileDialog(
            mode = Mode.LOAD
        ) { filePath ->
            isInputFileChooserOpen = false
            if (filePath != null) {
                onInputFilePathSelect(filePath)
            }
        }
    }

    var isExportFilePathChooserOpen by remember { mutableStateOf(false) }
    if (isExportFilePathChooserOpen) {
        FileDialog(
            mode = Mode.SAVE
        ) { filePath ->
            isExportFilePathChooserOpen = false
            if (filePath != null) {
                onExportFilePathSelect(filePath)
            }
        }
    }

    LazyColumn(
        modifier = Modifier.padding(
            top = 24.dp,
            start = 24.dp,
            end = 24.dp
        ),
        contentPadding = PaddingValues(
            bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        stickyHeader {
            Surface(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    if (uiState.isDictionaryLoading) {
                        Text("Dictionary Loading...")
                        return@Column
                    }
                    SearchForm(
                        keyword = uiState.keyword,
                        selectedPartOfSpeech = uiState.selectedPartOfSpeech ?: "",
                        partOfSpeeches = uiState.partOfSpeeches,
                        onKeywordChange = onKeywordChange,
                        onInputFileSelectorButtonClick = { isInputFileChooserOpen = true },
                        onPartOfSpeechChange = onPartOfSpeechChange,
                    )
                    SearchResultHeader(
                        modifier = Modifier.padding(bottom = 12.dp),
                        numberOfCards = uiState.numberOfCards,
                        isExportButtonEnabled = uiState.isExportButtonEnabled,
                        onFileExportButtonClick = { isExportFilePathChooserOpen = true },
                    )
                }
            }
        }
        if (uiState.isDictionaryLoading.not()) {
            uiState.cards?.forEachIndexed { index, card ->
                item(index) {
                    Card(
                        state = card
                    )
                }
            }
        }
    }
}
