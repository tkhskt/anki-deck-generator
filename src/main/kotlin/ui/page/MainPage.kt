package com.tkhskt.ankideckgenerator.ui.page

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.tkhskt.ankideckgenerator.ui.template.MainScreen

@Composable
fun MainPage(viewModel: MainViewModel = remember { MainViewModel() }) {
    LaunchedEffect(Unit) {
        viewModel.init()
    }

    val uiState by viewModel.uiState.collectAsState(MainUiState.Empty)

    MainScreen(
        uiState = uiState,
        onKeywordChange = { viewModel.changeKeyword(it) },
        onPartOfSpeechChange = { viewModel.changePartOfSpeech(it) },
        onInputFilePathSelect = { viewModel.searchByFile(it) },
        onExportFilePathSelect = { viewModel.exportDeck(it) }
    )
}
