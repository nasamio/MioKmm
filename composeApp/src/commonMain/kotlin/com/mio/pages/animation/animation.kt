package com.mio.pages.animation

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import miokmm.composeapp.generated.resources.Res
import miokmm.composeapp.generated.resources.test
import org.jetbrains.compose.resources.painterResource
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

private enum class ImageState {
    Small, Large
}

@Composable
fun animationUi() {
    Column {
        Row() {
            HoverImage(
                modifier = Modifier.width(200.dp)
            )
            val check = remember { mutableStateOf(false) }
            Switch(
                checked = check.value,
                onCheckedChange = { check.value = it },
                modifier = Modifier
            )
            MioSwitch(
                modifier = Modifier.width(56.dp)
                    .height(48.dp),
                checked = check.value,
                onCheckedChange = { check.value = it }
            )
            MioAnimateValue(
                modifier = Modifier.width(200.dp)
                    .height(400.dp)
            )

            MioTabRow(
                modifier = Modifier.width(200.dp)
                    .height(400.dp),
            )
        }

        MioAnimateSymbol(
            modifier = Modifier.fillMaxWidth()
                .height(400.dp)
                .background(Color.Black.copy(alpha = .1f))
        )
    }
}

@Composable
fun MioAnimateSymbol(modifier: Modifier) {
    // 使用 infiniteTransition 来创建无限循环动画
    val infiniteTransition = rememberInfiniteTransition()
    val animatedPathLength by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val path = Path().apply {
            moveTo(100f, 150f)
            cubicTo(
                200f, 300f,
                300f, 0f,
                400f, 200f
            )
        }

        val measurePath = Path().apply {
            addPath(path)
        }

        val measure = PathMeasure()
        measure.setPath(measurePath, false)
        val length = measure.length

        val animatedPath = Path()
        measure.getSegment(0f, animatedPathLength * length, animatedPath, true)

        drawPath(
            path = animatedPath,
            brush = SolidColor(Color.Red),
            style = Stroke(
                width = 5f,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}

@Composable
fun MioBezier(modifier: Modifier) {
    Canvas(modifier = modifier.border(1.dp, Color.Black)) {

    }
}

@Composable
fun MioTabRow(modifier: Modifier) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Tab 1", "Tab 2", "Tab 3")

    var tabHeight by remember { mutableStateOf(0.dp) }

    Column(
        modifier = modifier,
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth()
                .graphicsLayer {
                    tabHeight = size.height.toDp()
                },
            backgroundColor = Color.Transparent,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex])
                        .offset(y = -tabHeight + TabRowDefaults.IndicatorHeight)
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    modifier = Modifier.drawWithContent {
                        drawContent()

                        // 如果是第一个 就在左右侧绘制竖线 如果不是 就在右侧绘制横线

                        val color = Color.Black.copy(alpha = .1f)
                        val strokeWidth = 1f
                        if (index == 0) {
                            drawLine(
                                color = color,
                                start = Offset(0f, 0f),
                                end = Offset(0f, size.height),
                                strokeWidth = strokeWidth
                            )
                        }

                        drawLine(
                            color = color,
                            start = Offset(size.width, 0f),
                            end = Offset(size.width, size.height),
                            strokeWidth = strokeWidth
                        )
                    },
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            color = if (selectedTabIndex == index) Color(0xff1e80ff) else Color.Black,
                        )
                    },
                    selectedContentColor = Color.Transparent,
                    unselectedContentColor = Color.Transparent,
                )
            }
        }
        when (selectedTabIndex) {
            0 -> Text("Content for Tab 1")
            1 -> Text("Content for Tab 2")
            2 -> Text("Content for Tab 3")
        }
    }
}

@Composable
fun MioAnimateValue(modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var sides by remember { mutableStateOf(4f) }
        var length by remember { mutableStateOf(30.dp.value) }


        // 绘制一个多边形
        Canvas(
            modifier = Modifier.size(100.dp)
                .border(1.dp, Color.Black)
        ) {
            val pi = 3.141592653589793
            val cx = size.width / 2f
            val cy = size.height / 2f

            Path().apply {
                repeat(sides.roundToInt()) {
                    if (it == 0) {
                        moveTo(
                            (cx + length * cos(it * 2 * pi / sides)).toFloat(),
                            (cy + length * sin(it * 2 * pi / sides)).toFloat()
                        )
                    } else {
                        lineTo(
                            (cx + length * cos(it * 2 * pi / sides)).toFloat(),
                            (cy + length * sin(it * 2 * pi / sides)).toFloat()
                        )
                    }
                }
                close()
                rotate(-90f) {
                    drawPath(
                        path = this@apply,
                        color = Color.Black,
                        style = Stroke(
                            width = 2f,
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round,
                        )
                    )
                }

            }
        }


        MioSeekBar(
            modifier = Modifier.fillMaxWidth(),
            value = sides,
            onValueChange = { sides = it },
            step = 20,
        )
        Text("边数:$sides")
        MioSeekBar(
            modifier = Modifier.fillMaxWidth(),
            value = length,
            onValueChange = { length = it },
            step = 48.dp.value.toInt(),
        )
        Text("边长:$length")
    }
}

@Composable
fun MioSeekBar(
    modifier: Modifier,
    value: Int,
    onValueChange: (Int) -> Unit,
    step: Int = 0, // 分几挡 大于2
) {
    MioSeekBar(modifier, value.toFloat(), { onValueChange(it.toInt()) }, step)
}

@Composable
fun MioSeekBar(
    modifier: Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    step: Int = 0, // 分几挡 大于2
) {
    Slider(
        modifier = modifier,
        value = value,
        onValueChange = { onValueChange(it) },
        valueRange = 0f..step.toFloat(), // 进度范围
        steps = step - 1 // 中间的刻度数
    )
}

@Composable
fun MioSwitch(modifier: Modifier, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    // 组件宽高
    var composeSize by remember { mutableStateOf(Size.Zero) }

    val transition = updateTransition(targetState = checked, label = "ImageState Transition")

    val trackWidth = composeSize.height / 3f
    val radius = trackWidth * .72f
    val paddingHorizontal = composeSize.width / 20f

    val circleX by transition.animateFloat(label = "checkXPos") {
        if (it) (composeSize.width - (radius + paddingHorizontal)).toFloat()
        else radius + paddingHorizontal
    }
    val circleColor by transition.animateColor(label = "checkColor") {
        if (it) Color(0xff018786) else Color.White
    }
    val radiusStrokeWidth by transition.animateFloat { if (it) 0f else .3f }

    val trackColor by transition.animateColor(label = "checkColor") {
        if (it) Color(0xff76bebe) else Color.Gray
    }




    Canvas(
        modifier = modifier
            .graphicsLayer {
                // 获取组件宽高
                composeSize = size
            }
            .clickable {
                onCheckedChange(!checked)
            }
    ) {
        // 居中绘制背景轨道
        drawRoundRect(
            color = trackColor,
            topLeft = Offset(radius + paddingHorizontal, size.height / 2f - trackWidth / 2f),
            size = Size(size.width - (radius + paddingHorizontal) * 2f, trackWidth),
            cornerRadius = CornerRadius(trackWidth / 2f, trackWidth / 2f),
        )

        // 绘制thumb
        drawCircle(
            color = circleColor,
            radius = radius,
            center = Offset(
                circleX, size.height / 2f
            ),
        )
        // 如果有stroke绘制一圈
        if (radiusStrokeWidth > 0) {
            drawCircle(
                color = Color.Black.copy(alpha = .5f),
                radius = radius + radiusStrokeWidth,
                center = Offset(
                    circleX, size.height / 2f
                ),
                style = Stroke(radiusStrokeWidth),
            )
        }
    }
}

@Composable
fun HoverImage(
    modifier: Modifier,
) {
    var imageState by remember { mutableStateOf(ImageState.Small) }
    val transition = updateTransition(targetState = imageState, label = "ImageState Transition")

    val interactionSource = remember { MutableInteractionSource() }
    val hoveredAsState = interactionSource.collectIsHoveredAsState()

    LaunchedEffect(hoveredAsState.value) {
        imageState = if (hoveredAsState.value) {
            ImageState.Large
        } else {
            ImageState.Small
        }
    }

    val borderColor by transition.animateColor(label = "ImageState Color Transition") {
        when (it) {
            ImageState.Small -> Color.Green
            ImageState.Large -> Color.Magenta
        }
    }

    val size by transition.animateDp(label = "ImageState Size Transition") {
        when (it) {
            ImageState.Small -> 90.dp
            ImageState.Large -> 240.dp
        }
    }

    val rotation by transition.animateFloat(label = "ImageState Rotation Transition") {
        when (it) {
            ImageState.Small -> 0f
            ImageState.Large -> 360f
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(Res.drawable.test),
            contentDescription = null,
            modifier = Modifier
                .size(size)
                .rotate(rotation)
                .clip(shape = CircleShape)
                .border(color = borderColor, shape = CircleShape, width = 3.dp)
                .clickable(
                    indication = null,
                    interactionSource = interactionSource,
                ) { }
        )

        Button(
            onClick = {
                imageState = if (imageState == ImageState.Small) {
                    ImageState.Large
                } else {
                    ImageState.Small
                }
            }
        ) {
            Text(text = "切换")
        }
    }
}

@androidx.compose.desktop.ui.tooling.preview.Preview
@Composable
fun animationPreview() {
    animationUi()
}