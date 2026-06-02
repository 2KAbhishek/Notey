package com.iam2kabhishek.notey.locale

import kotlin.jvm.JvmInline

private const val LANG_ZH = "zh"
private const val SCRIPT_HANS = "Hans"
private const val SCRIPT_HANT = "Hant"

private val TRADITIONAL_CHINESE_REGIONS = setOf("TW", "HK", "MO")

object SupportedLocales {
    val supported: Set<String> = setOf("es", "hi", "zh-CN", "zh-TW")
}

/**
 * A type-safe, normalized BCP-47 locale tag (e.g., "en-US", "es", "zh-Hans-CN").
 * Normalization: Lowercase language, optional Titlecase script, optional uppercase region separated by '-'.
 */
@JvmInline
value class AppLocale(val tag: String) {
    init {
        require(tag.isNotEmpty()) { "Locale tag cannot be empty" }
    }

    val language: String get() = tag.substringBefore('-')

    val script: String? get() {
        val parts = tag.split('-')
        if (parts.size <= 1) return null
        return parts.drop(1).firstOrNull { isScriptSegment(it) }
    }

    val region: String? get() {
        val parts = tag.split('-')
        if (parts.size <= 1) return null
        val regionPart = parts.drop(1).firstOrNull { isRegionSegment(it) }
        return if (regionPart != null && regionPart.all { it.isDigit() }) regionPart else regionPart?.uppercase()
    }

    val resolvedScript: String? get() {
        val explicit = script
        if (explicit != null) return explicit
        if (language == LANG_ZH) {
            return if (region in TRADITIONAL_CHINESE_REGIONS) SCRIPT_HANT else SCRIPT_HANS
        }
        return null
    }

    companion object {
        fun from(raw: String): AppLocale? {
            val normalized = normalizeLocaleTag(raw)
            return if (normalized.isNotEmpty()) AppLocale(normalized) else null
        }
    }
}

/** Normalize BCP-47 style tag to lowercase language, titlecase script, and uppercase region. */
fun normalizeLocaleTag(raw: String): String {
    val t = raw.trim().replace('_', '-')
    if (t.isEmpty()) return ""
    val parts = t.split('-').filter { it.isNotEmpty() }
    if (parts.isEmpty()) return ""

    val lang = parts[0].lowercase()

    var script: String? = null
    var region: String? = null

    for (i in 1 until parts.size) {
        val part = parts[i]
        if (isScriptSegment(part)) {
            script = part.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        } else if (isRegionSegment(part)) {
            region = if (part.all { it.isDigit() }) part else part.uppercase()
        }
    }

    return buildString {
        append(lang)
        if (script != null) {
            append('-')
            append(script)
        }
        if (region != null) {
            append('-')
            append(region)
        }
    }
}

private fun isScriptSegment(segment: String): Boolean {
    return segment.length == 4 && segment.all { it.isLetter() }
}

private fun isRegionSegment(segment: String): Boolean {
    return (segment.length == 2 && segment.all { it.isLetter() }) ||
        (segment.length == 3 && segment.all { it.isDigit() })
}

/**
 * Pick the first system locale that matches [supported], most-specific first:
 * 1. Exact tag match (e.g., system wants "es-MX", app has "es-MX").
 * 2. Language + Script match (e.g., system wants "zh-Hant", app has "zh-TW" -> resolves to Hant).
 * 3. Language + Region match.
 * 4. Language-only fallback (ensuring script mismatches are avoided).
 */
fun pickBestLocale(
    system: List<String>,
    supported: Set<String>,
    defaultLocale: String = "en",
): String {
    val supportedLocales = supported.mapNotNull { AppLocale.from(it) }
    if (supportedLocales.isEmpty()) {
        return normalizeLocaleTag(defaultLocale).ifEmpty { "en" }
    }

    for (raw in system) {
        val sys = AppLocale.from(raw) ?: continue

        // 1. Exact match
        val exactMatch = supportedLocales.firstOrNull { it.tag == sys.tag }
        if (exactMatch != null) return exactMatch.tag

        // 2. Language + Script match
        val sysScript = sys.resolvedScript
        val scriptMatch = supportedLocales.firstOrNull { sup ->
            sup.language == sys.language && sup.resolvedScript == sysScript
        }
        if (scriptMatch != null) return scriptMatch.tag

        // 3. Language + Region match
        val regionMatch = supportedLocales.firstOrNull { sup ->
            sup.language == sys.language && sup.region == sys.region
        }
        if (regionMatch != null) return regionMatch.tag

        // 4. Language only match (fallback)
        val langMatch = supportedLocales.firstOrNull { sup ->
            sup.language == sys.language && (sysScript == null || sup.resolvedScript == null || sup.resolvedScript == sysScript)
        }
        if (langMatch != null) return langMatch.tag
    }

    // Default fallback
    val defaultNorm = normalizeLocaleTag(defaultLocale).ifEmpty { "en" }
    val defaultLocaleObj = AppLocale.from(defaultNorm)
    if (defaultLocaleObj != null) {
        val match = supportedLocales.firstOrNull { it.tag == defaultLocaleObj.tag }
            ?: supportedLocales.firstOrNull {
                it.language == defaultLocaleObj.language &&
                    (defaultLocaleObj.resolvedScript == null || it.resolvedScript == defaultLocaleObj.resolvedScript)
            }
        if (match != null) return match.tag
    }
    return defaultNorm
}

/** Split canonical tag for Compose resource qualifiers: language plus optional region subtag. */
fun splitTagForResourceEnvironment(canonicalTag: String): Pair<String, String?> {
    val locale = AppLocale.from(canonicalTag) ?: AppLocale("en")
    return locale.language to locale.region
}
