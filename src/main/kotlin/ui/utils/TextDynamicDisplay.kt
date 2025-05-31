package com.axxonsoft.utils.ui

import androidx.compose.foundation.basicMarquee
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun TextAutoSize(
    modifier: Modifier = Modifier,
    text: String,
    minSize: TextUnit = 9.sp,
    maxSize: TextUnit = MaterialTheme.typography.h6.fontSize,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
) {
    val textStyleBody1 = MaterialTheme.typography.body2.copy(fontSize = maxSize)
    var textStyle by remember { mutableStateOf(textStyleBody1) }
    var readyToDraw by remember { mutableStateOf(false) }
    Text(
        modifier = Modifier
            .drawWithContent { if (readyToDraw) drawContent() }
            .then(modifier),
        text = text,
        style = textStyle,
        textAlign = TextAlign.Center,
        color = color,
        softWrap = false,
        fontWeight = fontWeight,
        onTextLayout = { textLayoutResult ->
            if (textLayoutResult.didOverflowWidth && textStyle.fontSize > minSize) {
                textStyle = textStyle.copy(fontSize = textStyle.fontSize * 0.95)
            } else {
                readyToDraw = true
            }
        }
    )
}

@Composable
fun TextMarquee(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: TextUnit = MaterialTheme.typography.h6.fontSize,
    style: TextStyle = MaterialTheme.typography.body2.copy(fontSize = fontSize),
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
) {
    Text(
        modifier = Modifier
            .basicMarquee(
                iterations = Int.MAX_VALUE,
                velocity = 40.dp
            )
            .then(modifier),
        text = text,
        style = style,
        textAlign = TextAlign.Center,
        color = color,
        softWrap = false,
        fontWeight = fontWeight,
    )
}
