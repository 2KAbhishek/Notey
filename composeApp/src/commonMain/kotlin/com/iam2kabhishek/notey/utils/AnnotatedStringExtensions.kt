package com.iam2kabhishek.notey.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight

/**
 * Applies a given SpanStyle to all occurrences of the specified substrings within a text.
 */
fun AnnotatedString.Builder.addStyleToSubstrings(
    fullText: String,
    style: SpanStyle,
    vararg substrings: String
) {
    substrings.forEach { substring ->
        if (substring.isEmpty()) return@forEach
        var startIndex = fullText.indexOf(substring)
        while (startIndex >= 0) {
            addStyle(
                style = style,
                start = startIndex,
                end = startIndex + substring.length
            )
            // Handle multiple occurrences of the same substring
            startIndex = fullText.indexOf(substring, startIndex + substring.length)
        }
    }
}

/**
 * Applies a bold style to all occurrences of the specified substrings within a text.
 */
fun AnnotatedString.Builder.boldSubstrings(
    fullText: String,
    vararg substrings: String
) {
    addStyleToSubstrings(
        fullText = fullText,
        style = SpanStyle(fontWeight = FontWeight.Bold),
        substrings = substrings
    )
}
