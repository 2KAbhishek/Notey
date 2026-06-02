package com.iam2kabhishek.notey

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

import org.jetbrains.compose.resources.stringResource
import notey.composeapp.generated.resources.Res
import notey.composeapp.generated.resources.app_name

import com.iam2kabhishek.notey.locale.ProvidePrioritizedAppResources

fun main() = application {
    ProvidePrioritizedAppResources {
        Window(
            onCloseRequest = ::exitApplication,
            title = stringResource(Res.string.app_name),
        ) {
            App(Unit)
        }
    }
}
