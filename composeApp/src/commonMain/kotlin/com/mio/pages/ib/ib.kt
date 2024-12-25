package com.mio.pages.ib

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
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
import bean.ib.IbFile
import bean.smms.SmmsImage
import coil3.compose.AsyncImage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import utils.IbHelper

@Composable
fun IbUi() {
    val ibList = IbState.ibList.collectAsState()

    LaunchedEffect(1) {
        IbHelper.apply {
            println("listFiles: start...")
            listFiles().collect {
                println("listFiles: $it")
                if (it.success) {
                    IbState.ibList.value = it.data
                }
            }
        }
    }

    Column {
        Title()
        LazyVerticalGrid(
            columns = GridCells.FixedSize(240.dp),
            modifier = Modifier.fillMaxSize()
                .background(color = Color.DarkGray),
            contentPadding = PaddingValues(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp), // 控制横向间隔
            verticalArrangement = Arrangement.spacedBy(10.dp) // 控制纵向间隔
        ) {
            items(ibList.value.size) {
                IbItem(ibList.value[it])
            }
        }
    }

}

@Composable
fun Title() {
    Row(
        modifier = Modifier.fillMaxWidth()
            .background(Color.LightGray)
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            modifier = Modifier.width(80.dp)
                .height(40.dp),
            onClick = {
                GlobalScope.launch {
                    IbState.choose2Upload()
                }
            }) {
            Text(text = "上传")
        }
    }
}

@Composable
fun IbItem(item: IbFile) {
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
            Text(text = item.filename, style = MaterialTheme.typography.body1)
        }
    }
}
