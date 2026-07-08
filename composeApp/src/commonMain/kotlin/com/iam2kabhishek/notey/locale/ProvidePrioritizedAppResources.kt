package com.iam2kabhishek.notey.locale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.jetbrains.compose.resources.ProvideResourceEnvironment

/**
 * Applies prioritized system locale fallback to Compose Resources for the subtree.
 */
@Composable
fun ProvidePrioritizedAppResources(content: @Composable () -> Unit) {
    val systemLocales = remember { getSystemPreferredLocales() }
    val best = remember(systemLocales) {
        pickBestLocale(systemLocales, SupportedLocales.supported, defaultLocale = "en")
    }

    val (language, region) = remember(best) {
        splitTagForResourceEnvironment(best)
    }
    ProvideResourceEnvironment(language = language, region = region, content = content)
}
