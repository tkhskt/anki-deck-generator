package com.tkhskt.ankideckgenerator.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val LightDefaultColorScheme = lightColors(
    primary = AnkiDeckGeneratorColors.blue,
    onPrimary = AnkiDeckGeneratorColors.white,
    secondary = AnkiDeckGeneratorColors.orange,
    background = AnkiDeckGeneratorColors.lightGray,
    onSecondary = Color.White,
    surface = AnkiDeckGeneratorColors.lightGray,
    onSurface = AnkiDeckGeneratorColors.black,
)

object AnkiDeckGeneratorColors {
    val white = Color.White
    val blue = Color(0xFF0F87FF)
    val orange = Color(0xFFFFA33B)
    val lightGray = Color(0xFFEDEDED)
    val black = Color(0xFF292929)
}

internal val LocalColorPalette = staticCompositionLocalOf { AnkiDeckGeneratorColors }

object AnkiDeckGeneratorTheme {
    val color
        @Composable
        get() = LocalColorPalette.current
}

@Composable
fun AnkiDeckGeneratorTheme(
    content: @Composable () -> Unit,
) {
    val colors = AnkiDeckGeneratorColors
    CompositionLocalProvider(
        LocalColorPalette provides colors,
    ) {
        MaterialTheme(
            colors = LightDefaultColorScheme,
            content = content,
        )
    }
}
