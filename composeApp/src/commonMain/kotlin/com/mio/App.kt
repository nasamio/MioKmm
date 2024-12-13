@file:Suppress("UNUSED_EXPRESSION")

package com.mio

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.mio.pages.main.MainState
import com.mio.pages.main.MainUi
import kotlinx.coroutines.launch
import miokmm.composeapp.generated.resources.Res
import miokmm.composeapp.generated.resources.microsoft_yahei_simpli
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.ui.tooling.preview.Preview
import utils.NetHelper

@Composable
@Preview
fun App() {
    val scope = rememberCoroutineScope()
    scope.launch {
        println("App: start test...")
        val test = NetHelper.test()
        println("App: $test")
        MainState.toast(test)
    }

    MaterialTheme(
        typography = configTypography(),
    ) {
        MainUi()
    }
}

@Composable
fun configTypography(): Typography {
    val chineseFontFamily = FontFamily(Font(Res.font.microsoft_yahei_simpli, FontWeight.Normal))
    return Typography(defaultFontFamily = chineseFontFamily)
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

