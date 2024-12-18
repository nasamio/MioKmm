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
import kotlinx.coroutines.delay

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
            model = "https://img2.baidu.com/it/u=3068199002,3726366595&fm=253&fmt=auto&app=138&f=JPEG?w=1200&h=800",
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