package org.kmp.playground.donate.button

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "DonateButton",
    ) {
        App()
    }
}