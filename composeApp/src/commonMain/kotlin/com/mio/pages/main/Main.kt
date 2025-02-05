package com.mio.pages.main

import Animation2
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mio.pages.ai.aiUi
import com.mio.pages.bezier.Bezier
import com.mio.pages.animation.animationUi
import com.mio.pages.home.HomeUi
import com.mio.pages.login.LoginState
import com.mio.pages.login.LoginUi
import com.mio.pages.ib.IbUi
import com.mio.pages.map.MapUi
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun MainUi() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        val navController = rememberNavController()
        val studyMode = MainState.studyMode.collectAsState()
        val isLogin = LoginState.isLogin.collectAsState()

        NavHost(
            modifier = Modifier.fillMaxSize(),
            navController = navController,
            startDestination = studyMode.value.ifEmpty { if (isLogin.value) "home" else "smms" }
        ) {
            composable("login") {
                LoginUi()
            }
            composable("home") {
                HomeUi()
            }
            composable("smms") {
                IbUi()
            }
            composable("animation") {
                animationUi()
            }
            composable("animation2") {
                Animation2()
            }
            composable("bezier") {
                Bezier()
            }
            composable("map") {
                MapUi()
            }
            composable("ai") {
                aiUi()
            }
        }

        // toast
        val showText = MainState.showToast.collectAsState()
        val alpha by animateFloatAsState(
            targetValue = if (showText.value.isNotEmpty()) 1f else 0f,
            animationSpec = tween(durationMillis = 800) // 动画持续时间
        )

        if (showText.value.isNotEmpty()) {
            Column(
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Column(
                    modifier = Modifier
                        .background(Color.Gray, shape = RoundedCornerShape(5.dp))
                        .padding(10.dp)
                        .alpha(alpha) // 设置透明度
                        .animateContentSize() // 动态调整大小
                ) {
                    Text(
                        text = showText.value,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}