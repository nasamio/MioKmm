package com.mio

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.flow.MutableStateFlow
import miokmm.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource

val isMax = MutableStateFlow(false)

/**
 * 测试复刻 qqnt 效果
 */

@Composable
fun copyQq() {
    val max = isMax.collectAsState()


    val windowState = rememberWindowState(
        width = 960.dp,
        height = 730.dp,
    )

    Window(
        onCloseRequest = { copyQq.value = false },
        undecorated = true,
        state = windowState,
        transparent = true,
        icon = painterResource(Res.drawable.ic_qq),
        title = "QQ"
    ) {
        LaunchedEffect(max.value) {
            windowState.placement = if (max.value) WindowPlacement.Maximized else WindowPlacement.Floating
        }


        WindowDraggableArea(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFd5f4ff),
                                Color(0xFFfcfdff),
                            ),
                            start = Offset(0f, 0f),
                            end = Offset(windowState.size.width.value, windowState.size.height.value),
                        ),
                    )
            ) {
                Ui()
            }
        }
    }
}


/**
 * 分为 左侧 中 右 右侧包含关闭 最小化 最大化
 */
@Composable
fun FrameWindowScope.Ui() {
    Row {
        Left(
            modifier = Modifier.fillMaxHeight()
                .width(600.dp)
        )
        Mid(
            modifier = Modifier.fillMaxHeight()
                .width(305.dp)
                .background(Color.LightGray)
        )
        Right(
            modifier = Modifier.fillMaxHeight()
                .fillMaxWidth()
        )
    }
}

@Composable
fun Left(modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(30.dp))

        Avatar(Modifier.size(360.dp))
    }
}

fun customCircleShape(): Shape {
    return GenericShape { size, _ ->
        // 创建一个完整的圆形
        val path = Path().apply {
            addOval(Rect(0f, 0f, size.width, size.height))
        }

        // 创建一个小圆形的路径
        val cutoutPath = Path().apply {
            addOval(
                Rect(
                    size.width / 2f,
                    size.height / 2f,
                    size.width,
                    size.height
                )
            )
        }

        // 通过路径合并来实现扣除效果
        path.op(cutoutPath, path, PathOperation.ReverseDifference)
        addPath(path)
    }
}

@Composable
fun Avatar(modifier: Modifier) {
    Box(modifier = modifier) {
        Image(
            modifier = modifier.clip(customCircleShape())
                .align(Alignment.Center),
            painter = painterResource(Res.drawable.ic_user),
            contentDescription = null,
        )

        Box(Modifier.align(Alignment.BottomEnd)){
            Image(
                modifier = modifier.clip(CircleShape)
                    .scale(0.5f),
                painter = painterResource(Res.drawable.ic_user_status),
                contentDescription = null,
            )
        }
    }
}

@Composable
fun Mid(modifier: Modifier) {
    Column(
        modifier = modifier
    ) { }
}

@Composable
fun FrameWindowScope.Right(modifier: Modifier) {
    Column(
        modifier = modifier
    ) {
        // 顶部按钮
        Row(
            modifier = Modifier.fillMaxWidth()
                .height(32.dp),
            horizontalArrangement = Arrangement.End,
        ) {
            HoveredBox(modifier = Modifier.size(32.dp),
                onClick = { }) {
                Icon(
                    modifier = Modifier.size(16.dp).rotate(-90f),
                    painter = painterResource(Res.drawable.ic_expand),
                    contentDescription = null,
                )
            }

            HoveredBox(modifier = Modifier.size(32.dp),
                onClick = {
                    // 最小化
                    window.isMinimized = true
                }) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(Res.drawable.ic_min),
                    contentDescription = null,
                )
            }

            HoveredBox(modifier = Modifier.size(32.dp),
                onClick = {
                    // 最大化
                    isMax.value = !isMax.value
                }) {
                val max = isMax.collectAsState()

                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(if (max.value) Res.drawable.ic_max2 else Res.drawable.ic_max),
                    contentDescription = null,
                )
            }

            val isCloseHovered = remember { mutableStateOf(false) }
            HoveredBox(
                modifier = Modifier.size(32.dp),
                hoverColor = Color(0xffc42b1c),
                onHoverChange = {
                    isCloseHovered.value = it
                },
                onClick = {
                    copyQq.value = false
                },
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(Res.drawable.ic_close),
                    contentDescription = null,
                    tint = if (isCloseHovered.value) Color.White else LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
                )
            }
        }
    }
}


