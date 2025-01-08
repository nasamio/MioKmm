package com.mio.pages.bezier

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import miokmm.composeapp.generated.resources.Res
import miokmm.composeapp.generated.resources.testSvg
import org.jetbrains.compose.resources.painterResource
import kotlin.math.pow
import kotlin.math.sqrt

data class Pic(
    var graphs: MutableList<Graph>
)

data class Graph(
    var points: MutableList<Point>,
)

data class Point(
    val name: String,
    var x: Float,
    var y: Float,
    val color: Color = Color.Red,
)

/**
 * 二阶贝塞尔曲线 由三个控制点组成
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Bezier(
    modifier: Modifier = Modifier
) {
    var boxSize by remember { mutableStateOf(Size.Zero) }

    Box(modifier = modifier.padding(10.dp)
        .graphicsLayer {
            boxSize = size
        }) {
        val scale = 24 // 刻度尺 x和y只能在这个范围内
        val factor = 600f / scale // 比例系数
        val gridColor by remember { mutableStateOf(Color.LightGray) }

        val lineStrokeWidth by remember { mutableStateOf(1f * factor) }

        var pic by remember {
            mutableStateOf(
                Pic(
                    mutableListOf(
                        Graph(
                            mutableListOf(
                                Point("A", 2f, 2f, Color.Red),
                                Point("B", 13f, 3f, Color.Blue),
                                Point("C", 14f, 2f, Color.LightGray),
                                Point("D", 12f, 14f, Color.LightGray),
                            )
                        ),
                        Graph(
                            mutableListOf(
                                Point("A", 12f, 2f, Color.Red),
                                Point("B", 13f, 13f, Color.Blue),
                                Point("C", 14f, 2f, Color.LightGray),
                            )
                        ),
                    )
                )
            )
        }


        // 存储选中的点索引
        var selectGraphIndex = -1
        var selectPointIndex = -1

        val textMeasurer = rememberTextMeasurer()
        val textStyle = TextStyle(fontSize = 12.sp, color = Color.Black)
        val pointRadius by remember { mutableStateOf(10f) }

        var graphicsLayer: GraphicsLayer? = null

        Image(
            modifier = Modifier.size(600.dp)
                .align(Alignment.CenterStart),
            painter = painterResource(Res.drawable.testSvg),
            contentDescription = null,
        )


        Canvas(
            modifier = modifier.size(600.dp)
                .border(width = 1.dp, color = Color.Black)
                .align(Alignment.CenterStart)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            // 检测选中点
                            pic.graphs.forEachIndexed { gIndex, graph ->
                                graph.points.forEachIndexed { pIndex, point ->
                                    if (point.distanceTo(offset, factor) <= pointRadius) {
                                        selectGraphIndex = gIndex
                                        selectPointIndex = pIndex
                                    }
                                }
                            }
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            // 如果有选中的点，则更新其位置
                            if (selectGraphIndex != -1 && selectPointIndex != -1) {
                                val updatedGraph = pic.graphs[selectGraphIndex].apply {
                                    points[selectPointIndex] = points[selectPointIndex].copy(
                                        x = (points[selectPointIndex].x + dragAmount.x / factor),
                                        y = (points[selectPointIndex].y + dragAmount.y / factor)
                                    )
                                }
                                pic = pic.copy(graphs = pic.graphs.toMutableList().apply {
                                    this[selectGraphIndex] = updatedGraph
                                })
                            }

                        },
                        onDragEnd = {
                            // 拖拽结束后，释放选中的点
                            selectGraphIndex = -1
                            selectPointIndex = -1
                        }
                    )
                }
                .graphicsLayer {
                    alpha = .8f
                }
        ) {
            println("graphicsLayer:${drawContext.graphicsLayer}")

            drawIntoCanvas {
                it.save()

                graphicsLayer = drawContext.graphicsLayer
            }

            // 绘制背景 网格
            for (i in 0..size.width.toInt() step (size.width / scale).toInt()) {
                drawLine(
                    color = gridColor,
                    start = Offset(i.toFloat(), 0f),
                    end = Offset(i.toFloat(), size.height),
                    strokeWidth = 1f
                )
                drawLine(
                    color = gridColor,
                    start = Offset(0f, i.toFloat()),
                    end = Offset(size.width, i.toFloat()),
                    strokeWidth = 1f
                )
            }

            pic.graphs.forEach {
                val points = it.points
                when (it.points.size) {
                    1 -> { // 绘制点

                    }

                    2 -> { // 绘制线

                    }

                    3 -> { // 绘制二阶贝塞尔
                        Path().apply {
                            moveTo(points[0].x * factor, points[0].y * factor)
                            quadraticTo(
                                points[1].x * factor,
                                points[1].y * factor,
                                points[2].x * factor,
                                points[2].y * factor,
                            )
                            drawPath(
                                this,
                                color = Color.Black,
                                style = Stroke(width = lineStrokeWidth)
                            )
                        }
                    }

                    4 -> { // 绘制三阶贝塞尔
                        Path().apply {
                            moveTo(points[0].x * factor, points[0].y * factor)
                            cubicTo(
                                points[1].x * factor,
                                points[1].y * factor,
                                points[2].x * factor,
                                points[2].y * factor,
                                points[3].x * factor,
                                points[3].y * factor,
                            )
                            drawPath(
                                this,
                                color = Color.Black,
                                style = Stroke(width = lineStrokeWidth)
                            )
                        }
                    }
                }
            }
            // 绘制可拖拽的点
            pic.graphs.forEach {
                it.points.forEach { point ->
                    // 限制点的中心
                    point.x =
                        if (point.x < 0) 0f else if (point.x > size.width) size.width else point.x
                    point.y =
                        if (point.y < 0) 0f else if (point.y > size.height) size.height else point.y

                    drawCircle(
                        point.color,
                        radius = pointRadius,
                        center = Offset(point.x * factor, point.y * factor)
                    )

                    // 绘制文字
                    val text = "${point.name}(${point.x.round2Text()},${point.y.round2Text()})"
                    val textLayoutResult = textMeasurer.measure(text, textStyle)

                    val y =
                        if (point.y * factor + pointRadius + 5f + textLayoutResult.size.height > size.height) {
                            point.y * factor - pointRadius - 5f - textLayoutResult.size.height
                        } else {
                            point.y * factor + pointRadius + 5f
                        }
                    drawText(
                        textMeasurer = textMeasurer,
                        text = text,
                        style = textStyle,
                        topLeft = Offset(
                            point.x * factor - textLayoutResult.size.width / 2,
                            y
                        )
                    )

                }
            }
        }

        val scope = rememberCoroutineScope()
        Column(
            modifier = Modifier.height(600.dp)
                .width(boxSize.width.dp - 600.dp)
                .align(Alignment.CenterEnd)
                .border(width = 1.dp, color = Color.Black)
                .padding(10.dp)
        ) {
            var img by remember { mutableStateOf<ImageBitmap?>(null) }

            Button(onClick = {
                scope.launch {
                    println("导出1")
                    graphicsLayer?.let {
                        println("开始导出...")
                        toBitmap(it) {
                            println("导出成功")
                            img = it
                        }
                    }
                }
            }) {
                Text("导出为png")
            }

            img?.let {
                Image(
                    modifier = Modifier.size(200.dp),
                    bitmap = it,
                    contentDescription = null
                )
            }
        }
    }
}

suspend fun toBitmap(graphicsLayer: GraphicsLayer, onRes: (ImageBitmap) -> Unit) {
    graphicsLayer.toImageBitmap()
        .let {
            onRes(it)
        }
}

fun Float.round2Text(len: Int = 1): String {
    val indexOf = this.toString().indexOf(".")
    return this.toString().substring(0, indexOf + len + 1)
}

fun Point.distanceTo(other: Offset, factor: Float = 1f): Float {
    return sqrt((this.x * factor - other.x).pow(2) + (this.y * factor - other.y).pow(2))
}
