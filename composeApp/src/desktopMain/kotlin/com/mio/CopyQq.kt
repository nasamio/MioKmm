package com.mio

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.compose.ui.window.*
import kotlinx.coroutines.flow.MutableStateFlow
import miokmm.composeapp.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
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
        position = WindowPosition(Alignment.Center),
    )

    Window(
        onCloseRequest = { copyQq.value = false },
        undecorated = true,
        state = windowState,
        transparent = true,
        icon = painterResource(Res.drawable.ic_qq),
        title = "QQ",

    ) {
        LaunchedEffect(max.value) {
            windowState.placement = if (max.value) WindowPlacement.Maximized else WindowPlacement.Floating
        }


        WindowDraggableArea(
            modifier = Modifier.fillMaxSize()
                .border(width = 1.dp, color = Color.Black.copy(alpha = 0.1f))
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xffd5f4ff),
                                Color(0xfffcfdff),
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
                .width(60.dp)
        )
        Mid(
            modifier = Modifier.fillMaxHeight()
                .width(305.dp),
        )
        Right(
            modifier = Modifier.fillMaxHeight()
                .fillMaxWidth()
        )
    }
}

@Composable
fun Left(modifier: Modifier) {
    Box(modifier = modifier) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(30.dp))

            Avatar(36.dp)

            val iconList = mutableListOf(
                Res.drawable.ic_message,
                Res.drawable.ic_contact,
                Res.drawable.ic_star,
                Res.drawable.ic_campaign,
                Res.drawable.ic_game,
                Res.drawable.ic_menu,
            )
            val checkPos = remember { mutableStateOf(0) }
            Spacer(Modifier.height(10.dp))

            iconList.fastForEachIndexed { index, drawableResource ->
                Spacer(Modifier.height(8.dp))

                Ic(
                    modifier = Modifier.size(38.dp),
                    resource = drawableResource,
                    checked = checkPos.value == index,
                ) {
                    checkPos.value = index
                }

            }
        }

        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.Bottom,
        ) {
            val iconList = mutableListOf(
                Res.drawable.ic_mail,
                Res.drawable.ic_file,
                Res.drawable.ic_bookmark,
                Res.drawable.ic_menu2,
            )

            iconList.forEach {
                HoverIcon(
                    modifier = Modifier.size(28.dp),
                    resource = it,
                    hoverTintColor = Color(0xff26b9f3),
                    onClick = {
                    }
                )

                Spacer(Modifier.height(12.dp))
            }
            Spacer(Modifier.height(1.dp))
        }
    }
}

@Composable
fun Ic(
    modifier: Modifier,
    resource: DrawableResource,
    checked: Boolean = false,
    onClick: () -> Unit = {}
) {
    HoveredBox(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        isCheck = checked,
        onClick = { onClick() }
    ) {
        Icon(
            painter = painterResource(resource),
            contentDescription = null,
            modifier = Modifier.size(28.dp),
            tint = if (checked) Color(0xff26b9f3) else Color.DarkGray,
        )
    }
}

fun customCircleShape(innerSize: Float = 0.3f): Shape {
    return GenericShape { size, _ ->
        // 创建一个完整的圆形
        val path = Path().apply {
            addOval(Rect(0f, 0f, size.width, size.height))
        }

        // 创建一个小圆形的路径
        val cutoutPath = Path().apply {
            addOval(
                Rect(
                    size.width * (1 - 2 * innerSize),
                    size.height * (1 - 2 * innerSize),
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
fun Avatar(size: Dp) {
    val innerSize = 0.2f // 内圆半径比例

    Box(modifier = Modifier.size(size)) {
        Image(
            modifier = Modifier.clip(customCircleShape(innerSize))
                .size(size)
                .align(Alignment.Center),
            painter = painterResource(Res.drawable.ic_user),
            contentDescription = null,
        )

        Box(Modifier.align(Alignment.BottomEnd)) {
            Image(
                modifier = Modifier.clip(CircleShape)
                    .size(size * 2 * innerSize)
                    .align(Alignment.BottomEnd),
                painter = painterResource(Res.drawable.ic_user_status),
                contentDescription = null,
            )
        }
    }
}

@Composable
fun Mid(modifier: Modifier) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    Column(
        modifier = modifier
            .onGloballyPositioned {
                size = it.size
            }
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xffd9f5ff),
                        Color(0xffdfedff),
                    ),
                    start = Offset(size.width.toFloat() / 2f, 0f),
                    end = Offset(size.width.toFloat() / 2f, size.height.toFloat()),
                ),
            ).drawWithContent {
                drawContent()
                // 右侧绘制一条从上到下的线
                drawLine(
                    color = Color.Black.copy(alpha = 0.3f),
                    start = Offset(size.width.toFloat() - 1f, 0f),
                    end = Offset(size.width.toFloat() - 1f, size.height.toFloat()),
                    strokeWidth = .2f,
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(28.dp))

        SearchArea()

        Spacer(Modifier.height(10.dp))

        MessageList(Modifier.fillMaxSize())
    }
}

data class Message(
    val avatar: DrawableResource,
    val name: String,
    val msg: String,
    val timeStr: String,
    val msgSize: Int,
)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MessageList(modifier: Modifier) {
    val testMsgList = remember {
        mutableListOf<Message>().apply {
            repeat(10) {
                add(
                    Message(
                        avatar = Res.drawable.test,
                        name = "Mio ddddddddddddddddddddddddddd",
                        msg = "Hello, World!2222222222222222222222222222",
                        timeStr = "12:00",
                        msgSize = 1,
                    )
                )
                add(
                    Message(
                        avatar = Res.drawable.ic_user,
                        name = "Mio",
                        msg = "Hello, World!",
                        timeStr = "12:00",
                        msgSize = 10,
                    )
                )
            }
        }
    }
    val checkPos = remember { mutableStateOf(0) }

    // 创建一个状态来控制 LazyColumn 的滚动状态
    val scrollState = rememberLazyListState()

    Box(modifier = modifier) {
        LazyColumn(
            state = scrollState,
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(testMsgList) { index, item ->
                MessageItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp),
                    message = item,
                    isCheck = index == checkPos.value,
                    onClick = {
                        checkPos.value = index
                    }
                )
            }
        }

        // 添加滚动条
        VerticalScrollbar(
            adapter = rememberScrollbarAdapter(scrollState),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .width(8.dp) // 设置滚动条的宽度
                .clickable { },
            style = LocalScrollbarStyle.current.copy(
                minimalHeight = 80.dp
            )
        )
    }
}

@Composable
fun MessageItem(
    modifier: Modifier,
    message: Message,
    isCheck: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val hoveredAsState = interactionSource.collectIsHoveredAsState()

    Row(
        modifier = modifier.background(
            if (isCheck) {
                Color(0xff28c1fd)
            } else if (hoveredAsState.value) {
                Color.Black.copy(alpha = .05f)
            } else {
                Color.Transparent
            }
        ).clickable(
            indication = null,
            interactionSource = interactionSource,
        ) {
            onClick()
        }.padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier.size(40.dp)
                .clip(CircleShape),
            painter = painterResource(message.avatar),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )

        Spacer(Modifier.width(10.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = message.name,
                fontSize = 12.sp,
                color = Color.Black.copy(.8f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = message.msg,
                fontSize = 12.sp,
                color = Color.Black.copy(.8f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Spacer(Modifier.width(10.dp))

        Column(
            horizontalAlignment = Alignment.End,
        ) {
            Text(
                text = message.timeStr,
                fontSize = 12.sp,
                color = Color.Black.copy(.8f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.height(8.dp))
            if (message.msgSize > 0) {
                Box(
                    modifier = Modifier.size(20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isCheck) Color(0xff209aca) else Color(0xffafc2cc)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = message.msgSize.toString(),
                        fontSize = 12.sp,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }


}

@Composable
fun SearchArea() {
    var isFocused by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        var text by remember { mutableStateOf("") }
        var showDelete by remember { mutableStateOf(false) }

        BasicTextField(
            modifier = Modifier.weight(1f)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },
            value = text,
            onValueChange = {
                text = it
                showDelete = it.isNotEmpty()
            },
            singleLine = true,
            textStyle = TextStyle.Default.copy(
                color = Color.Black.copy(.8f),
                fontSize = 12.sp,
                textAlign = TextAlign.Start,
                lineHeight = 26.sp,
            ),
            cursorBrush = SolidColor(Color(0xff26b9f3)),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .height(26.dp)
                            .background(Color.Black.copy(alpha = .05f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(Res.drawable.ic_search),
                            contentDescription = null,
                            tint = Color.Black.copy(alpha = .5f),
                        )
                        Spacer(Modifier.width(5.dp))
                        innerTextField()
                    }

                    // 提示文字
                    if (text.isEmpty() && !isFocused) {
                        Text(
                            text = "搜索",
                            style = TextStyle(
                                color = Color.Black.copy(alpha = 0.5f),
                                fontSize = 12.sp,
                                textAlign = TextAlign.Start,
                                lineHeight = 26.sp,
                            ),
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = 30.dp) // 适当的左侧内边距以避免与图标重叠
                                .align(Alignment.Center)
                        )
                    }


                    if (showDelete) {
                        Row(
                            modifier = Modifier.align(Alignment.CenterEnd)
                        ) {
                            Icon(
                                modifier = Modifier.size(14.dp)
                                    .clickable {
                                        text = ""
                                        showDelete = false
                                    },
                                painter = painterResource(Res.drawable.ic_close),
                                contentDescription = null,
                                tint = Color.Black.copy(alpha = .4f),
                            )
                            Spacer(Modifier.width(8.dp))
                        }
                    }
                }
            }
        )
        Spacer(Modifier.width(8.dp))

        HoveredBox(
            modifier = Modifier.size(28.dp),
            shape = RoundedCornerShape(4.dp),
            defaultColor = Color.Black.copy(alpha = 0.04f),
            hoverColor = Color.Black.copy(alpha = 0.1f),
            onClick = {}) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = Icons.Rounded.Add,
                contentDescription = null,
                tint = Color(0xff9ab2bf)
            )
        }
    }
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


