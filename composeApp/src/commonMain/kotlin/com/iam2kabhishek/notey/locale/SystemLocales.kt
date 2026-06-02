package com.iam2kabhishek.notey.locale

/**
 * System locale preferences in descending priority (user's first choice first).
 * Tags are normalized via [normalizeLocaleTag].
 */
expect fun getSystemPreferredLocales(): List<String>

/**
 * Synchronizes the global JVM/Android default locale with the selected [tag].
 */
expect fun setPlatformDefaultLocale(tag: String)
