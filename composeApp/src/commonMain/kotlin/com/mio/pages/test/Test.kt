package com.mio.pages.test

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TestUi() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
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