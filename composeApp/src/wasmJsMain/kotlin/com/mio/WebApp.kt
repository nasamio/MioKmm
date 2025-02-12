package com.mio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.ExperimentalBrowserHistoryApi
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.bindToNavigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mio.PageControl.navController
import com.mio.pages.help.HelpUi
import com.mio.pages.home.HomeUi
import com.mio.pages.home.WebHomeUi
import kotlinx.browser.window
import kotlinx.coroutines.flow.MutableStateFlow
import miokmm.composeapp.generated.resources.Res
import miokmm.composeapp.generated.resources.microsoft_yahei_simpli
import org.jetbrains.compose.resources.Font
import org.w3c.dom.PopStateEvent


@OptIn(ExperimentalBrowserHistoryApi::class)
@Composable
fun WebApp() {
    MaterialTheme(typography = configTypography2()) {


        navController = rememberNavController()

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            NavHost(
                modifier = Modifier.fillMaxSize(),
                navController = navController,
                startDestination = "home"
            ) {
                composable("home") {
                    WebHomeUi()
                }
                composable("help") {
                    HelpUi()
                }
            }

            LaunchedEffect(Unit) {
                window.bindToNavigation(navController)
            }
        }
    }
}

@Composable
fun configTypography2(): Typography {
    val chineseFontFamily = FontFamily(Font(Res.font.microsoft_yahei_simpli, FontWeight.Normal))
    return Typography(defaultFontFamily = chineseFontFamily)
}