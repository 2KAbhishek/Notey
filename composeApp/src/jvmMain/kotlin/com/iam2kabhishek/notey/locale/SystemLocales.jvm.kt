package com.iam2kabhishek.notey.locale

import com.sun.jna.platform.win32.Advapi32Util
import com.sun.jna.platform.win32.WinReg
import java.util.Locale

actual fun getSystemPreferredLocales(): List<String> {
    val os = System.getProperty("os.name")?.lowercase().orEmpty()
    if (os.contains("win")) {
        getWindowsPreferredLocales()?.let { return it }
    }
    return getFallbackLocale()
}

private fun getWindowsPreferredLocales(): List<String>? {
    return try {
        val languagesArray = Advapi32Util.registryGetStringArray(
            WinReg.HKEY_CURRENT_USER,
            "Control Panel\\International\\User Profile",
            "Languages"
        )
        languagesArray.mapNotNull { lang ->
            val normalized = normalizeLocaleTag(lang)
            normalized.takeIf { it.isNotEmpty() }
        }.distinct().takeIf { it.isNotEmpty() }
    } catch (_: Throwable) {
        null
    }
}

private fun getFallbackLocale(): List<String> {
    val fallback = normalizeLocaleTag(Locale.getDefault().toLanguageTag().replace('_', '-'))
    return if (fallback.isNotEmpty()) listOf(fallback) else listOf("en")
}

actual fun setPlatformDefaultLocale(tag: String) {
    try {
        val locale = Locale.forLanguageTag(tag)
        if (Locale.getDefault() != locale) {
            Locale.setDefault(locale)
        }
    } catch (_: Exception) {
    }
}
