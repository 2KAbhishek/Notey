package com.iam2kabhishek.notey.locale

import kotlin.jvm.JvmInline

private val TRADITIONAL_CHINESE_REGIONS = setOf("TW", "HK", "MO")

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

    val script: String?
        get() {
            val parts = tag.split('-')
            if (parts.size <= 1) return null
            return parts.drop(1).firstOrNull { isScriptSegment(it) }
        }

    val region: String?
        get() {
            val parts = tag.split('-')
            if (parts.size <= 1) return null
            val regionPart = parts.drop(1).firstOrNull { isRegionSegment(it) }
            return if (regionPart != null && regionPart.all { it.isDigit() }) regionPart else regionPart?.uppercase()
        }

    val resolvedScript: String?
        get() {
            val explicit = script
            if (explicit != null) return explicit
            if (language == "zh") {
                return if (region in TRADITIONAL_CHINESE_REGIONS) "Hant" else "Hans"
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
 * Pick the first system locale that matches [supported] (implicitly including [defaultLocale]),
 * most-specific first:
 * 1. Exact tag match (e.g., system wants "es-MX", app has "es-MX").
 * 2. Matching Region (e.g., system wants "zh-Hans-CN", app has "zh-CN" / "zh-Hans-CN").
 * 3. Base / Neutral Language (e.g., system wants "en-IN", app has "en" (generic/base)).
 * 4. Different Region (e.g., system wants "en-IN", app has "en-GB" (fallback)).
 */
fun pickBestLocale(
    system: List<String>,
    supported: Set<String>,
    defaultLocale: String = "en",
): String {
    val defaultNorm = normalizeLocaleTag(defaultLocale).ifEmpty { "en" }
    val extendedSupported = supported + defaultNorm
    val supportedLocales = extendedSupported.mapNotNull { AppLocale.from(it) }

    for (raw in system) {
        val sys = AppLocale.from(raw) ?: continue

        // 1. Exact match
        val exactMatch = supportedLocales.firstOrNull { it.tag == sys.tag }
        if (exactMatch != null) return exactMatch.tag

        val sysScript = sys.resolvedScript

        // 2. Matching Region
        val regionMatch = supportedLocales.firstOrNull { sup ->
            sup.language == sys.language &&
                    (sysScript == null || sup.resolvedScript == null || sup.resolvedScript == sysScript) &&
                    sup.region != null && sup.region == sys.region
        }
        if (regionMatch != null) return regionMatch.tag

        // 3. Base / Neutral Language match (no region)
        val baseMatch = supportedLocales.firstOrNull { sup ->
            sup.language == sys.language &&
                    (sysScript == null || sup.resolvedScript == null || sup.resolvedScript == sysScript) &&
                    sup.region == null
        }
        if (baseMatch != null) return baseMatch.tag

        // 4. Different Region match (fallback)
        val diffRegionMatch = supportedLocales.firstOrNull { sup ->
            sup.language == sys.language &&
                    (sysScript == null || sup.resolvedScript == null || sup.resolvedScript == sysScript)
        }
        if (diffRegionMatch != null) return diffRegionMatch.tag
    }

    // Default fallback
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
