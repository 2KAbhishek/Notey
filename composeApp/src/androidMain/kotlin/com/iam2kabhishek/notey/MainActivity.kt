package com.iam2kabhishek.notey

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Centralized process-wide startup default locale setup to address Point 2
        try {
            val systemLocales = com.iam2kabhishek.notey.locale.getSystemPreferredLocales()
            val bestLocaleTag = com.iam2kabhishek.notey.locale.pickBestLocale(
                systemLocales,
                com.iam2kabhishek.notey.locale.SupportedLocales.supported,
                defaultLocale = "en"
            )
            com.iam2kabhishek.notey.locale.setPlatformDefaultLocale(bestLocaleTag)
        } catch (_: Exception) {
        }

        setContent {
            com.iam2kabhishek.notey.locale.ProvidePrioritizedAppResources {
                App(this)
            }
        }
    }
}
