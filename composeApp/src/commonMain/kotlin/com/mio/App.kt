@file:Suppress("UNUSED_EXPRESSION")

package com.mio

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.mio.pages.main.MainUi
import com.mio.pages.test.TestState
import com.mio.pages.test.TestUi
import kotlinx.coroutines.launch
import miokmm.composeapp.generated.resources.Res
import miokmm.composeapp.generated.resources.microsoft_yahei_simpli
import okio.FileSystem
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.ui.tooling.preview.Preview
import utils.NetHelper

@OptIn(ExperimentalCoilApi::class)
@Composable
@Preview
fun App() {
    val scope = rememberCoroutineScope()
    scope.launch {


    }

    MaterialTheme(
        typography = if (isWasm()) configTypography() else MaterialTheme.typography,
    ) {
        initCoil()

        if (TestState.testMode) {
            TestUi()
        } else {
            MainUi()
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun initCoil() {
    println("initCoil: 初始化coil开始...")
    // 初始化coil
    setSingletonImageLoaderFactory { context ->
//        ImageLoader.Builder(context).memoryCachePolicy(CachePolicy.DISABLED).memoryCache {
//            MemoryCache.Builder().maxSizePercent(context, 0.3).strongReferencesEnabled(false).build()
//        }.diskCachePolicy(CachePolicy.DISABLED).networkCachePolicy(CachePolicy.DISABLED)
//            .crossfade(true).logger(DebugLogger()).build()

        ImageLoader.Builder(context).build()
    }
    println("initCoil: 初始化coil结束...")
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

