package com.tkhskt.ankideckgenerator.ui.organization

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.tkhskt.ankideckgenerator.ui.AnkiDeckGeneratorTheme

data class CardState(
    val front: String,
    val back: String
)

@Composable
fun Card(
    state: CardState,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        SelectionContainer {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Half(
                    modifier = Modifier.padding(bottom = 12.dp),
                    value = state.front
                )
                Divider(modifier = Modifier.fillMaxWidth())
                Half(
                    modifier = Modifier.padding(top = 12.dp),
                    value = state.back
                )
            }
        }
    }
}

@Composable
private fun Half(
    value: String,
    modifier: Modifier = Modifier,
) {
    val clipboard = LocalClipboardManager.current
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(bottom = 12.dp),
            text = value
        )
        CopyIcon(
            modifier = Modifier.align(Alignment.TopEnd),
            onClick = { clipboard.setText(AnnotatedString(value)) }
        )
    }
}

@Composable
private fun CopyIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .clickable { onClick() }
            .padding(4.dp)
            .width(20.dp)
            .height(14.dp)
            .alpha(0.7f)
    ) {
        Box(
            modifier = Modifier
                .background(AnkiDeckGeneratorTheme.color.lightGray)
                .width(16.dp)
                .height(10.dp)
                .border(2.dp, Color.Gray)
                .align(Alignment.TopStart)
        )
        Box(
            modifier = Modifier
                .background(AnkiDeckGeneratorTheme.color.lightGray)
                .width(16.dp)
                .height(10.dp)
                .border(2.dp, Color.Gray)
                .align(Alignment.BottomEnd)
        )
    }
}
