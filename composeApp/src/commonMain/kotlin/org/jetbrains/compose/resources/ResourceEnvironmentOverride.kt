@file:Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")

package org.jetbrains.compose.resources

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember

/**
 * WARNING: This file relies on JetBrains internal APIs (InternalResourceApi) to provide
 * a way to programmatically override the resource environment (locale, theme, etc.).
 *
 * This is necessary because Compose Multiplatform (as of 1.8.x) does not yet provide
 * a public, stable API for manually forcing a specific locale onto its resource system.
 */
@OptIn(InternalResourceApi::class)
@Composable
fun ProvideResourceEnvironment(
    language: String? = null,
    region: String? = null,
    isDark: Boolean? = null,
    density: Float? = null,
    content: @Composable () -> Unit,
) {
    val currentComposeEnv = LocalComposeEnvironment.current
    val currentEnv = currentComposeEnv.rememberEnvironment()

    val newEnv = remember(currentEnv, language, region, isDark, density) {
        ResourceEnvironment(
            language = language?.let { LanguageQualifier(it) } ?: currentEnv.language,
            region = region?.let { RegionQualifier(it) } ?: currentEnv.region,
            theme = isDark?.let { ThemeQualifier.selectByValue(it) } ?: currentEnv.theme,
            density = density?.let { DensityQualifier.selectByDensity(it) } ?: currentEnv.density,
        )
    }

    val newComposeEnv = object : ComposeEnvironment {
        @Composable
        override fun rememberEnvironment(): ResourceEnvironment = newEnv
    }

    CompositionLocalProvider(
        LocalComposeEnvironment provides newComposeEnv,
        content = content,
    )
}
