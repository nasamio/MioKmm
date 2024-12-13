@file:Suppress("UNUSED_EXPRESSION")

package com.mio

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import com.mio.pages.main.MainUi
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        MainUi()
    }
}

@Composable
fun MioTheme(
    colors: Colors = MaterialTheme.colors,
    typography: Typography = MaterialTheme.typography,
    shapes: Shapes = MaterialTheme.shapes,
    content: @Composable () -> Unit
) {
    @Suppress("DEPRECATION_ERROR")
    CompositionLocalProvider(
        LocalContentAlpha provides ContentAlpha.high,
    ) {
        ProvideTextStyle(value = typography.body1) {
            MaterialTheme {
                content
            }
        }
    }
}

