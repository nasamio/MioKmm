package com.mio.pages.bezier

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.draggable2D
import androidx.compose.foundation.gestures.rememberDraggable2DState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.pow
import kotlin.math.sqrt

data class Point(
    val name: String,
    var x: Float,
    var y: Float
)

/**
 * 二阶贝塞尔曲线 由三个控制点组成
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Bezier(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.padding(10.dp),
    ) {
        val circleRadius by remember { mutableStateOf(10f) }
        // 使用 mutableStateOf 来存储点的状态
        val points = remember {
            mutableStateListOf(
                Point("A", 100f, 100f),
                Point("B", 300f, 300f),
                Point("C", 500f, 100f)
            )
        }
        var checkIndex = -1

        val textMeasurer = rememberTextMeasurer()
        val textStyle = TextStyle(color = Color.White, fontSize = 16.sp)

        Canvas(
            modifier = modifier
                .size(600.dp)
                .align(Alignment.CenterStart)
                .border(width = 1.dp, color = Color.Black)
                .draggable2D(
                    state = rememberDraggable2DState {
                        println("drag: $it")
                        if (checkIndex >= 0) {
                            points[checkIndex].let {
                                println("当前拖拽的点($it)")
                                it.x += it.x
                                it.y += it.y
                            }
                        }

                    },
                    onDragStarted = {
                        println(" onDragStart:$it")
                        // 判断拽的是哪个点
                        points.forEach { p ->
                            if (it.distanceTo(Offset(p.x, p.y)) <= circleRadius) {
                                checkIndex = points.indexOf(p)
                                return@forEach
                            }
                        }
                    },
                    onDragStopped = {
                        println(" onDragStopped:$it")
                        checkIndex = -1
                    },
                )
        ) {
            println("width: ${size.width}, height: ${size.height}")
            Path().apply {
                moveTo(points[0].x, points[0].y)
                quadraticBezierTo(points[1].x, points[1].y, points[2].x, points[2].y)
                drawPath(
                    path = this,
                    color = Color.Red,
                    style = Stroke(width = 5f)
                )
            }

            // 绘制出 A、B、C点及坐标文字
            points.forEach { point ->
                val x = point.x
                val y = point.y
                drawCircle(
                    color = Color(0xff6fb5fd),
                    radius = circleRadius,
                    center = Offset(x, y)
                )

                // 绘制文字
                val idTextLayoutResult = textMeasurer.measure(point.name, textStyle)
                drawText(
                    textMeasurer = textMeasurer,
                    text = point.name,
                    style = textStyle,
                    topLeft = Offset(
                        x - idTextLayoutResult.size.width / 2,
                        y - idTextLayoutResult.size.height / 2
                    )
                )
            }
        }
    }
}

// 计算两个点之间的距离
fun Offset.distanceTo(other: Offset): Float {
    return sqrt((this.x - other.x).pow(2) + (this.y - other.y).pow(2))
}

fun Point.distanceTo(other: Point): Float {
    return sqrt((this.x - other.x).pow(2) + (this.y - other.y).pow(2))
}
