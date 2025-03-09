package com.tkhskt.ankideckgenerator.ui.organization

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.AwtWindow
import java.awt.FileDialog
import java.awt.Frame

@Composable
fun FileDialog(
    parent: Frame? = null,
    title: String = "Choose a file",
    mode: Mode,
    outputFile: String? = "deck.md",
    onCloseRequest: (filePath: String?) -> Unit
) = AwtWindow(
    create = {
        object : FileDialog(parent, title, mode.value) {

            init {
                if (mode == Mode.SAVE) {
                    outputFile?.let { setFile(outputFile) }
                }
            }

            override fun setVisible(value: Boolean) {
                super.setVisible(value)
                if (value && directory != null && file != null) {
                    onCloseRequest(directory + file)
                }
            }
        }
    },
    dispose = FileDialog::dispose
)

enum class Mode(val value: Int) {
    LOAD(FileDialog.LOAD),
    SAVE(FileDialog.SAVE)
}
