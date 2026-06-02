package com.iam2kabhishek.notey.locale

import android.os.LocaleList
import java.util.Locale

actual fun getSystemPreferredLocales(): List<String> {
    val list = LocaleList.getDefault()
    val out = ArrayList<String>()
    val size = list.size()
    for (i in 0 until size) {
        list.get(i)?.let { locale ->
            val tag = locale.toLanguageTag().replace('_', '-')
            if (tag.isNotEmpty()) {
                val n = normalizeLocaleTag(tag)
                if (n.isNotEmpty()) out.add(n)
            }
        }
    }
    return out.distinct()
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
