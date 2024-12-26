package com.mio.pages.ib

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import bean.ib.IbFile
import bean.smms.SmmsImage
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import com.mio.copyQq
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import utils.IbHelper

@Composable
fun IbUi() {
    val ibList = IbState.ibList.collectAsState()
    var selectedImageUrl by remember { mutableStateOf<String?>(null) }

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

    Box(
        modifier = Modifier.fillMaxSize()
            .background(color = Color.DarkGray)
    ) {
        Column {
            Title()
            LazyVerticalGrid(
                columns = GridCells.FixedSize(240.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(ibList.value.size) {
                    IbItem(ibList.value[it]) { imageUrl ->
                        selectedImageUrl = imageUrl
                    }
                }
            }


        }

        // 显示大图
        AnimatedVisibility(
            visible = selectedImageUrl != null,
            enter = fadeIn(animationSpec = tween(durationMillis = 300)), // 进入动画
            exit = fadeOut(animationSpec = tween(durationMillis = 300)) // 退出动画
        ) {
            FullScreenImageDialog(selectedImageUrl.toString()) {
                selectedImageUrl = null // 关闭对话框
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
fun IbItem(item: IbFile, onImageClick: (String) -> Unit) {
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
                modifier = Modifier
                    .size(240.dp)
                    .clickable { onImageClick(item.url) }, // 点击时传递图片URL
                model = item.url,
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
            )
            Text(text = item.filename, style = MaterialTheme.typography.body1)
        }
    }
}

@Composable
fun FullScreenImageDialog(imageUrl: String, onDismiss: () -> Unit) {
    // 使用全屏对话框
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black) // 设置背景为黑色
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .clickable { onDismiss() } // 点击关闭对话框
                .align(Alignment.Center) // 居中显示图片
        )
    }
}
