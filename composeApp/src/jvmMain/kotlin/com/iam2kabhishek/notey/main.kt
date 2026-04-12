package com.iam2kabhishek.notey

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Notey",
    ) {
        App()
    }
}