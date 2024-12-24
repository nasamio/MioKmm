package com.mio.pages.test

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import kotlinx.coroutines.delay
import miokmm.composeapp.generated.resources.Res
import miokmm.composeapp.generated.resources.test
import miokmm.composeapp.generated.resources.vis
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import utils.SmmsHelper

@OptIn(ExperimentalResourceApi::class)
@Composable
fun TestUi() {


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // 网络图片加载
        AsyncImage(
            modifier = Modifier
                .size(250.dp)
                .padding(16.dp),
            model = "https://s2.loli.net/2024/12/24/zNZvkqOjQcn5b62.jpg", // 加载res
            contentDescription = null,
            onError = { }
        )
        Text("网络图片")

        // 本地图片加载
        Image(
            modifier = Modifier
                .size(250.dp)
                .padding(16.dp),
            painter = painterResource(Res.drawable.test),
            contentDescription = null,
        )
        Text("本地图片")



        TestState.testList.forEach {
            Spacer(modifier = Modifier.height(10.dp))
            Button(onClick = it.clickEvent) {
                Text(text = it.name)
            }
        }
    }
}

data class TestData(
    val name: String,
    val clickEvent: () -> Unit,
)