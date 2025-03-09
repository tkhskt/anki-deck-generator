package com.tkhskt.ankideckgenerator.ui.organization

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SearchResultHeader(
    numberOfCards: Int,
    isExportButtonEnabled: Boolean,
    onFileExportButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Result: $numberOfCards"
        )
        Button(
            onClick = onFileExportButtonClick,
            enabled = isExportButtonEnabled,
        ) {
            Text(
                text = "Export"
            )
        }
    }
}
