package com.tkhskt.ankideckgenerator.infra

import java.io.File

class CsvLoader {
    fun read(filePath: String): List<List<String>> {
        val file = File(filePath) // CSVファイルのパスを指定
        val result = mutableListOf<List<String>>()
        file.bufferedReader().useLines { lines ->
            lines.forEach { line ->
                val columns = line.split(",")
                result.add(listOf(columns.first(), columns[1]))
            }
        }
        return result
    }
}
