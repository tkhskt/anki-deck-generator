package com.tkhskt.ankideckgenerator.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.tkhskt.ankideckgenerator.ui.template.MainScreen

@Composable
fun App(viewModel: MainViewModel) {
    LaunchedEffect(Unit) {
        viewModel.init()
    }

    val uiState by viewModel.uiState.collectAsState(MainUiState.Empty)

    AnkiDeckGeneratorTheme {
        MainScreen(
            uiState = uiState,
            onKeywordChange = { viewModel.changeKeyword(it) },
            onPartOfSpeechChange = { viewModel.changePartOfSpeech(it) },
            onInputFilePathSelect = { viewModel.onInputFilePathSelect(it) },
            onExportFilePathSelect = { viewModel.onExportFilePathSelect(it) }
        )
    }
}

