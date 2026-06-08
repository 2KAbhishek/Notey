package com.iam2kabhishek.notey.locale

import kotlin.test.Test
import kotlin.test.assertEquals

class LocaleFallbackTest {
    @Test
    fun pickBest_mostSpecific_whenBothExist() {
        assertEquals(
            "es-MX",
            pickBestLocale(
                listOf("es-MX"),
                setOf("es", "es-MX"),
            ),
        )
    }

    @Test
    fun pickBest_languageOnly_whenRegionNotSupported() {
        assertEquals(
            "es",
            pickBestLocale(
                listOf("es-MX"),
                setOf("es"),
            ),
        )
    }

    @Test
    fun pickBest_default_whenNothingMatches() {
        assertEquals(
            "en",
            pickBestLocale(
                listOf("de-DE", "fr-FR"),
                setOf("en"),
            ),
        )
    }

    @Test
    fun pickBest_respectsUserPreference_matchingBaseLanguage() {
        assertEquals(
            "en",
            pickBestLocale(
                listOf("fr-CA", "en-IN", "es-MX"),
                setOf("es-MX", "en-US", "hi"),
            ),
        )
    }

    @Test
    fun normalizeLocaleTag_underscoreAndCase() {
        assertEquals("en-GB", normalizeLocaleTag("en_GB"))
        assertEquals("es-MX", normalizeLocaleTag("ES-mx"))
    }

    @Test
    fun normalizeLocaleTag_withScriptAndRegion() {
        assertEquals("zh-Hans-CN", normalizeLocaleTag("zh_Hans_CN"))
        assertEquals("zh-Hant-TW", normalizeLocaleTag("zh-Hant-TW"))
        assertEquals("sr-Cyrl-RS", normalizeLocaleTag("sr-Cyrl-RS"))
        assertEquals("zh-Hans", normalizeLocaleTag("zh-Hans"))
    }

    @Test
    fun pickBestLocale_traditionalChineseFallback() {
        assertEquals(
            "zh-TW",
            pickBestLocale(
                listOf("zh-Hant-US"),
                setOf("zh-CN", "zh-TW")
            )
        )
        assertEquals(
            "zh-TW",
            pickBestLocale(
                listOf("zh-Hant"),
                setOf("zh-CN", "zh-TW")
            )
        )
    }

    @Test
    fun pickBestLocale_simplifiedChineseFallback() {
        assertEquals(
            "zh-CN",
            pickBestLocale(
                listOf("zh-Hans"),
                setOf("zh-CN", "zh-TW")
            )
        )
    }

    @Test
    fun pickBestLocale_genericScriptMatching() {
        assertEquals(
            "sr-Cyrl-RS",
            pickBestLocale(
                listOf("sr-Cyrl"),
                setOf("sr-Latn-RS", "sr-Cyrl-RS")
            )
        )
    }

    @Test
    fun pickBestLocale_preferBaseLanguageOverMismatchedRegion() {
        assertEquals(
            "en",
            pickBestLocale(
                listOf("en-IN"),
                setOf("en-GB")
            )
        )
    }

    @Test
    fun pickBestLocale_userScenario_enIN_bn_esUS() {
        assertEquals(
            "en",
            pickBestLocale(
                listOf("en-IN", "bn", "es-US"),
                setOf("es", "hi", "zh-CN", "zh-TW")
            )
        )
    }
}
