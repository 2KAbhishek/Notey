package com.iam2kabhishek.notey

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

import org.jetbrains.compose.resources.stringResource
import notey.composeapp.generated.resources.Res
import notey.composeapp.generated.resources.app_name

import com.iam2kabhishek.notey.locale.ProvidePrioritizedAppResources
import com.iam2kabhishek.notey.locale.getSystemPreferredLocales
import com.iam2kabhishek.notey.locale.pickBestLocale
import com.iam2kabhishek.notey.locale.SupportedLocales
import com.iam2kabhishek.notey.locale.setPlatformDefaultLocale

fun main() {
    // Centralized process-wide startup default locale setup to address Point 2
    try {
        val systemLocales = getSystemPreferredLocales()
        val bestLocaleTag = pickBestLocale(systemLocales, SupportedLocales.supported, defaultLocale = "en")
        setPlatformDefaultLocale(bestLocaleTag)
    } catch (_: Exception) {
    }

    application {
        ProvidePrioritizedAppResources {
            Window(
                onCloseRequest = ::exitApplication,
                title = stringResource(Res.string.app_name),
            ) {
                App(Unit)
            }
        }
    }
}
