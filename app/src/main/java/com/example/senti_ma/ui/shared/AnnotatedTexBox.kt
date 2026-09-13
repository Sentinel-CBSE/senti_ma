package com.example.senti_ma.ui.shared

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

@Composable
fun AnnotatedTextBox(
    modifier: Modifier = Modifier,
    textClickable: String,
    onClick: () -> Unit,
    text: String = ""
){
    Box(modifier = modifier) {
        val annotatedText = buildAnnotatedString {
            append(text)

            val startIndex = length
            withStyle(style = SpanStyle(color = colorScheme.primary)) {
                append(textClickable)
            }
            val endIndex = length

            addStringAnnotation(
                tag = "CLICKABLE_TAG",
                annotation = "clickable",
                start = startIndex,
                end = endIndex
            )
        }

        Text(
            text = annotatedText,
            modifier = Modifier.clickable {
                annotatedText.getStringAnnotations(
                    tag = "CLICKABLE_TAG",
                    start = 0,
                    end = annotatedText.length
                ).firstOrNull()?.let { onClick() }
            }
        )
    }
}
