package com.mio

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import androidx.compose.ui.window.ComposeViewport
import io.ktor.client.fetch.*
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport(document.body!!) {
//        CanvasBasedWindow("Coil3 CMP") {

//        testFetch()

            App()
//        }
    }
}


