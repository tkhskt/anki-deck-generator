package com.tkhskt.ankideckgenerator

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.window.application
import com.tkhskt.ankideckgenerator.dictionary.Dictionary
import kotlinx.coroutines.runBlocking
import androidx.compose.ui.window.Window

fun main() = application {
    measure {
        try {
            val entries = generateEntries(
                inputFilePath = "input.csv",
                dictionaryPath = "dictionary/eijiro.txt"
            )
            val generator = DeckGenerator(
                fileName = "deck",
                cardSeparator = "\$break\$",
                frontBackSeparator = 'å'
            )
            generator.generate(entries)

        } catch (e: Exception) {
            println(e.toString())
        }
    }
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}

@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }

    MaterialTheme {
        Button(
            onClick = { text = "Hello, Desktop!" },
            modifier = Modifier.testTag("button")
        ) {
            Text(text)
        }
    }
}


private suspend fun generateEntries(
    inputFilePath: String,
    dictionaryPath: String,
): List<Dictionary.Entry> {
    val input = CsvLoader(inputFilePath).read()
    val dictionary: Dictionary = Dictionary.eijiro(dictionaryPath)
    val queries = input.map { query ->
        Dictionary.Query(
            keyword = query[0],
            partOfSpeech = query.getOrNull(1)?.let { Dictionary.PartOfSpeech.find(it) }
        )
    }
    val entries = dictionary.findAll(queries)
    return entries
}

private fun measure(block: suspend () -> Unit) {
    val start = System.currentTimeMillis()
    runBlocking {
        block()
    }
    val end = System.currentTimeMillis()
    println("Execution Time: ${end - start}ms")
}
