/*
 * Copyright (C) 2026 Lenovo
 * All Rights Reserved.
 * Lenovo Confidential Restricted.
 */

package com.iam2kabhishek.notey.locale

import org.jetbrains.compose.resources.LanguageQualifier
import org.jetbrains.compose.resources.RegionQualifier
import org.jetbrains.compose.resources.ResourceEnvironment
import org.jetbrains.compose.resources.ThemeQualifier
import org.jetbrains.compose.resources.DensityQualifier
import org.jetbrains.compose.resources.ProvideResourceEnvironment
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Canary test to guard the usage of JetBrains Compose Multiplatform's internal 
 * ResourceEnvironment APIs inside Notey.
 * 
 * To catch runtime binary compatibility issues (such as NoSuchMethodError or NoClassDefFoundError)
 * where the classes compile but fail to load or initialize on modern runtimes, this test 
 * directly instantiates and asserts on the internal Compose qualifier classes.
 * 
 * It also checks the Composable signature of ProvideResourceEnvironment to ensure that the 
 * Compose compiler plugin cleanly transforms and resolves the internal API usages.
 */
@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
class ResourceEnvironmentCanaryTest {

    @OptIn(org.jetbrains.compose.resources.InternalResourceApi::class)
    @Test
    fun testInternalComposeResourceAPIsExist() {
        // Directly exercise class loading and instantiation of internal qualifiers
        val languageQualifier = LanguageQualifier("pt")
        val regionQualifier = RegionQualifier("BR")
        
        val env = ResourceEnvironment(
            language = languageQualifier,
            region = regionQualifier,
            theme = ThemeQualifier.LIGHT,
            density = DensityQualifier.MDPI
        )
        
        assertNotNull(env)
        assertEquals("pt", env.language.language)
        assertEquals("BR", env.region.region)
    }

    @Test
    fun testResourceEnvironmentOverrideCompilesCleanlyUnderComposeCompiler() {
        // Assert that the Composable wrapper can be compiled, referenced, and initialized.
        val testComposable: @androidx.compose.runtime.Composable () -> Unit = {
            ProvideResourceEnvironment(language = "pt", region = "BR") {
                // No-op
            }
        }
        assertNotNull(testComposable)
    }
}
