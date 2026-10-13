package com.iam2kabhishek.notey

import androidx.compose.ui.window.Window
import platform.AppKit.NSApp
import platform.AppKit.NSApplication

fun main() {
    NSApplication.sharedApplication()
    Window("Notey") {
        App(Unit)
    }
    NSApp?.run()
}
