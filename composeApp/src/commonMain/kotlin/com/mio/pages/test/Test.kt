package com.mio.pages.test

import androidx.compose.animation.core.*
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
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun TestUi() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            modifier = Modifier
                .size(250.dp)
                .padding(16.dp),
            model = "https://www4.bing.com//th?id=OHR.SantaClausVillage_ZH-CN1839275027_1920x1080.jpg&rf=LaDigue_1920x1080.jpg&pid=hp&w=360&h=202", // 加载res
            contentDescription = null,
            onError = { }
        )

        AsyncImage(
            modifier = Modifier
                .size(250.dp)
                .padding(16.dp),
            model = Res.getUri("drawable/test.jpg"),
            contentDescription = null,
            onError = { }
        )


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