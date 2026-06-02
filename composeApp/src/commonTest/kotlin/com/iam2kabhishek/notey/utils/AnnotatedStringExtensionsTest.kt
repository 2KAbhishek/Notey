package com.iam2kabhishek.notey.utils

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AnnotatedStringExtensionsTest {

    @Test
    fun testBoldSubstringsAppliesStyleToSubstrings() {
        val fullText = "Made with ❤️ by 2KAbhishek"
        val author = "2KAbhishek"

        val annotated = buildAnnotatedString {
            append(fullText)
            boldSubstrings(fullText, author)
        }

        val styles = annotated.spanStyles
        assertEquals(1, styles.size)

        val authorStyle = styles.find { it.start == fullText.indexOf(author) }
        assertTrue(authorStyle != null)
        assertEquals(authorStyle.end, fullText.indexOf(author) + author.length)
        assertEquals(FontWeight.Bold, authorStyle.item.fontWeight)
    }

    @Test
    fun testMultipleOccurrencesAreBolded() {
        val fullText = "apple orange banana apple cherry apple"
        val word = "apple"

        val annotated = buildAnnotatedString {
            append(fullText)
            boldSubstrings(fullText, word)
        }

        val styles = annotated.spanStyles
        assertEquals(3, styles.size)
    }

    @Test
    fun testEmptyOrNonExistentSubstringsAreIgnored() {
        val fullText = "some text"

        val annotated = buildAnnotatedString {
            append(fullText)
            boldSubstrings(fullText, "", "non-existent")
        }

        assertTrue(annotated.spanStyles.isEmpty())
    }

    @Test
    fun testBoldStyleIsOnlyAppliedToTargetSubstringsAndNoOtherText() {
        val fullText = "The quick brown fox jumps over the lazy dog"
        val word = "quick"

        val annotated = buildAnnotatedString {
            append(fullText)
            boldSubstrings(fullText, word)
        }

        val styles = annotated.spanStyles

        val range = fullText.indexOf(word) until (fullText.indexOf(word) + word.length)

        for (index in fullText.indices) {
            val isExpectedBold = index in range
            val isActuallyBold = styles.any { index in it.start until it.end && it.item.fontWeight == FontWeight.Bold }

            assertEquals(isExpectedBold, isActuallyBold, "Character at index $index ('${fullText[index]}') bold status mismatch.")
        }
    }
}
