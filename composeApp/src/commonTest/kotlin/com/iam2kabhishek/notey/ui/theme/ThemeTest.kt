package com.iam2kabhishek.notey.ui.theme

import androidx.compose.ui.graphics.Color
import kotlin.test.Test
import kotlin.test.assertEquals

class ThemeTest {

    @Test
    fun testLightBlueColorsAreCorrect() {
        assertEquals(Color(0xFF0061A4), LightBlueColors.primary)
        assertEquals(Color(0xFFFFFFFF), LightBlueColors.onPrimary)
        assertEquals(Color(0xFFD1E4FF), LightBlueColors.primaryContainer)
        assertEquals(Color(0xFF001D36), LightBlueColors.onPrimaryContainer)
        assertEquals(Color(0xFFF8F9FF), LightBlueColors.background)
        assertEquals(Color(0xFF191C20), LightBlueColors.onBackground)
    }

    @Test
    fun testDarkBlueColorsAreCorrect() {
        assertEquals(Color(0xFF4FA3F7), DarkBlueColors.primary)
        assertEquals(Color(0xFF000000), DarkBlueColors.onPrimary)
        assertEquals(Color(0xFF00497C), DarkBlueColors.primaryContainer)
        assertEquals(Color(0xFFD1E4FF), DarkBlueColors.onPrimaryContainer)
        assertEquals(Color(0xFF000000), DarkBlueColors.background)
        assertEquals(Color(0xFFF1F5F9), DarkBlueColors.onBackground)
    }
}
