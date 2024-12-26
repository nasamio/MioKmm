package com.mio

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
//    Window(
//        onCloseRequest = ::exitApplication,
//        title = "MioKmm",
//    ) {
//        App()
//    }

    val copyQq = copyQq.collectAsState()
    if (copyQq.value){
        copyQq()
    }
}

