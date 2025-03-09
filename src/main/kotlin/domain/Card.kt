package com.tkhskt.ankideckgenerator.domain

data class Card(
    val front: String,
    val back: String,
) {
    companion object {
        const val SEPARATOR = "å"
    }
}
