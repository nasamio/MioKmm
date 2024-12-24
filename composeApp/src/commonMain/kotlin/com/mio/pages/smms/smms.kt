package com.mio.pages.smms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import bean.smms.SmmsImage
import coil3.compose.AsyncImage
import utils.SmmsHelper

@Composable
fun smmsUi() {
    val smmsList = smmsState.smmsList.collectAsState()

    LaunchedEffect(1) {
        SmmsHelper.apply {
            println("smmsUi: start...")
            uploadHistory().collect {
                println("uploadHistory: $it")
                if (it.success) {
                    smmsState.smmsList.value = it.data
                }
            }
        }
    }

    LazyVerticalGrid(
        columns = GridCells.FixedSize(240.dp),
        modifier = Modifier.fillMaxSize()
            .background(color = Color.DarkGray),
        contentPadding = PaddingValues(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp), // 控制横向间隔
        verticalArrangement = Arrangement.spacedBy(10.dp) // 控制纵向间隔
    ) {
        items(smmsList.value.size) {
            SmmsItem(smmsList.value[it])
        }
    }

}

@Composable
fun SmmsItem(item: SmmsImage) {
    Column {
        Column(
            modifier = Modifier
                .width(240.dp)
                .height(280.dp)
                .background(color = Color.LightGray, shape = RoundedCornerShape(8.dp))
                .padding(top = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AsyncImage(
                modifier = Modifier.size(240.dp),
                model = item.url,
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
            )
            Text(text = item.storename, style = MaterialTheme.typography.body1)
        }
    }
}
