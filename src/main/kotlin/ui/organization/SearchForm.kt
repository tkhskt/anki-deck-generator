package com.tkhskt.ankideckgenerator.ui.organization

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SearchForm(
    keyword: String,
    selectedPartOfSpeech: String,
    partOfSpeeches: List<String>,
    onKeywordChange: (String) -> Unit,
    onInputFileSelectorButtonClick: () -> Unit,
    onPartOfSpeechChange: (String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = keyword,
                onValueChange = onKeywordChange,
            )
            Button(
                modifier = Modifier.padding(start = 24.dp),
                onClick = onInputFileSelectorButtonClick
            ) {
                Text(
                    text = "Select File"
                )
            }

        }
        PartOfSpeechSelector(
            modifier = Modifier.padding(vertical = 12.dp),
            selectedValue = selectedPartOfSpeech,
            options = partOfSpeeches,
            onChange = onPartOfSpeechChange,
        )
    }
}

@Composable
private fun PartOfSpeechSelector(
    selectedValue: String,
    options: List<String>,
    onChange: (String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier.clickable {
                expanded = true
            },
            text = "Part Of Speech: $selectedValue",
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    onChange(null)
                }
            ) {}
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        onChange(option)
                    }
                ) {
                    Text(option)
                }
            }
        }
    }
}
