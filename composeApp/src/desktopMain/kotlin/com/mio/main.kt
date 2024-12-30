package com.mio

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    val windowState = rememberWindowState(
        width = 960.dp,
        height = 730.dp,
        position = WindowPosition(Alignment.Center),
    )
    Window(
        onCloseRequest = ::exitApplication,
        title = "MioKmm",
        state = windowState,
    ) {
        window.placement
        App()
    }

    val copyQq = copyQq.collectAsState()
    if (copyQq.value){
        copyQq()
    }
}

