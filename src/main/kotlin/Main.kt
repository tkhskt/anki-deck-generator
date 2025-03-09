package com.tkhskt.ankideckgenerator

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.tkhskt.ankideckgenerator.ui.App
import com.tkhskt.ankideckgenerator.ui.MainViewModel

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App(viewModel = MainViewModel())
    }
}
