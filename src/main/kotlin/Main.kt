package com.tkhskt.ankideckgenerator

import androidx.compose.ui.window.singleWindowApplication
import com.tkhskt.ankideckgenerator.ui.App

fun main() = singleWindowApplication(
    title = "Anki Deck Generator",
) {
    App()
}
